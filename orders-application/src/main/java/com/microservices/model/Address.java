package com.microservices.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class Address {
    @NotNull
    public String email;

    @NotNull
    public String country;

    @NotNull
    public String city;

    @NotNull
    public String street;

    @NotNull
    public int house;

    public int corp;

    @NotNull
    public int flat;

    public Address(@NotNull String email, @NotNull String country, @NotNull String city, @NotNull String street,
                   @NotNull int house, @NotNull int flat) {
        this.email = email;
        this.country = country;
        this.city = city;
        this.street = street;
        this.house = house;
        this.flat = flat;
    }

    public Address(@NotNull String email, @NotNull String country, @NotNull String city,
                   @NotNull String street, @NotNull int house, int corp, @NotNull int flat) {
        new Address(email,country,city,street,house,flat);
        this.corp = corp;
    }
}
