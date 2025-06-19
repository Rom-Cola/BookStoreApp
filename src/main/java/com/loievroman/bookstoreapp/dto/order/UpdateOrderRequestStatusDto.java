package com.loievroman.bookstoreapp.dto.order;

import com.loievroman.bookstoreapp.model.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderRequestStatusDto {
    @NotNull
    private Status status;
}
