package guru.qa.countryservice.service.grpc;

import com.google.protobuf.Empty;
import guru.qa.countryservice.dto.CountryRequest;
import guru.qa.countryservice.dto.CountryResponse;
import guru.qa.countryservice.dto.CountryUpdateRequest;
import guru.qa.countryservice.service.CountryService;
import guru.qa.grpc.countrycatalog.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@GrpcService
public class GrpcCountryService extends CountrycatalogServiceGrpc.CountrycatalogServiceImplBase {

    private final CountryService countryService;

    @Autowired
    public GrpcCountryService(CountryService countryService) {
        this.countryService = countryService;
    }

    @Override
    public void getCountry(GetCountryRequest request, StreamObserver<guru.qa.grpc.countrycatalog.CountryResponse> responseObserver) {
        try {
            CountryResponse country = countryService.getCountryByCode(request.getCode());
            responseObserver.onNext(mapToProtoCountry(country));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("Country not found with code: " + request.getCode())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void addCountry(guru.qa.grpc.countrycatalog.CountryRequest request,
                           StreamObserver<guru.qa.grpc.countrycatalog.CountryResponse> responseObserver) {
        try {
            CountryRequest dtoRequest = new CountryRequest();
            dtoRequest.setName(request.getName());
            dtoRequest.setCode(request.getCode());
            dtoRequest.setCoordinates(request.getCoordinates());

            CountryResponse country = countryService.createCountry(dtoRequest);
            responseObserver.onNext(mapToProtoCountry(country));
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            responseObserver.onError(
                    Status.ALREADY_EXISTS
                            .withDescription("Country with code " + request.getCode() + " already exists")
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void updateCountry(UpdateCountryRequest request,
                              StreamObserver<guru.qa.grpc.countrycatalog.CountryResponse> responseObserver) {
        try {
            CountryUpdateRequest updateRequest = new CountryUpdateRequest();
            updateRequest.setName(request.getName());

            CountryResponse country = countryService.updateCountry(request.getCode(), updateRequest);
            responseObserver.onNext(mapToProtoCountry(country));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("Country not found with code: " + request.getCode())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void deleteCountry(DeleteCountryRequest request,
                              StreamObserver<DeleteCountryResponse> responseObserver) {
        try {
            countryService.deleteCountry(request.getCode());

            responseObserver.onNext(
                    DeleteCountryResponse.newBuilder()
                            .setSuccess(true)
                            .setMessage("Country with code " + request.getCode() + " successfully deleted")
                            .build()
            );
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onNext(
                    DeleteCountryResponse.newBuilder()
                            .setSuccess(false)
                            .setMessage("Failed to delete country with code " + request.getCode() + ": " + e.getMessage())
                            .build()
            );
            responseObserver.onCompleted();
        }
    }

    @Override
    public void listCountries(Empty request,
                                StreamObserver<guru.qa.grpc.countrycatalog.CountryResponse> responseObserver) {
        try {
            List<CountryResponse> countries = countryService.listCountries();

            for (CountryResponse country : countries) {
                responseObserver.onNext(mapToProtoCountry(country));
            }
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Error fetching countries: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    /**
     * Стриминговое добавление стран
     * Client-streaming - принимает поток стран, возвращает статистику
     */
    @Override
    public StreamObserver<guru.qa.grpc.countrycatalog.CountryRequest> addCountriesStream(
            StreamObserver<AddCountriesResponse> responseObserver) {

        return new StreamObserver<guru.qa.grpc.countrycatalog.CountryRequest>() {
            private final AtomicInteger totalReceived = new AtomicInteger(0);
            private final AtomicInteger successfullyAdded = new AtomicInteger(0);
            private final List<String> failedCodes = new ArrayList<>();

            @Override
            public void onNext(guru.qa.grpc.countrycatalog.CountryRequest request) {
                totalReceived.incrementAndGet();

                try {
                    CountryRequest dtoRequest = new CountryRequest();
                    dtoRequest.setName(request.getName());
                    dtoRequest.setCode(request.getCode().toUpperCase());
                    dtoRequest.setCoordinates(request.getCoordinates());

                    countryService.createCountry(dtoRequest);
                    successfullyAdded.incrementAndGet();
                } catch (Exception e) {
                    failedCodes.add(request.getCode());
                    System.err.printf("Failed to add country %s (%s): %s%n",
                            request.getName(), request.getCode(), e.getMessage());
                }
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(
                        Status.INTERNAL
                                .withDescription("Error processing stream: " + t.getMessage())
                                .asRuntimeException()
                );
            }

            @Override
            public void onCompleted() {
                String message = String.format(
                        "Processed %d countries, successfully added: %d, failed: %d",
                        totalReceived.get(),
                        successfullyAdded.get(),
                        failedCodes.size()
                );

                responseObserver.onNext(
                        AddCountriesResponse.newBuilder()
                                .setTotalReceived(totalReceived.get())
                                .setSuccessfullyAdded(successfullyAdded.get())
                                .addAllFailedCodes(failedCodes)
                                .setMessage(message)
                                .build()
                );
                responseObserver.onCompleted();
            }
        };
    }

    private guru.qa.grpc.countrycatalog.CountryResponse mapToProtoCountry(CountryResponse country) {
        guru.qa.grpc.countrycatalog.CountryResponse.Builder builder =
                guru.qa.grpc.countrycatalog.CountryResponse.newBuilder()
                        .setId(country.getId())
                        .setName(country.getName())
                        .setCode(country.getCode());

        if (country.getCoordinates() != null) {
            builder.setCoordinates(country.getCoordinates());
        } else {
            builder.setCoordinates("");
        }

        return builder.build();
    }
}