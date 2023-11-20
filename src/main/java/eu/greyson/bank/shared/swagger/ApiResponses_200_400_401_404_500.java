package eu.greyson.bank.shared.swagger;

import eu.greyson.bank.shared.dto.ExceptionDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "SUCCESS"),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                content = @Content(
                        schema = @Schema(
                                implementation = ExceptionDto.class
                        )
                )
        ),
        @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                content = @Content(
                        schema = @Schema(
                                implementation = ExceptionDto.class
                        )
                )
        ),
        @ApiResponse(responseCode = "404", description = "ID NOT FOUND",
                content = @Content(
                        schema = @Schema(
                                implementation = ExceptionDto.class
                        )
                )
        ),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                content = @Content(
                        schema = @Schema(
                                implementation = ExceptionDto.class
                        )
                )
        ),
})
public @interface ApiResponses_200_400_401_404_500 {
}
