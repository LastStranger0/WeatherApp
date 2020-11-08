package com.testproject.weathermap

class City {
    var CityName: String
        set(value) {
            field = value
        }
        get(): String{
            return field
        }

    var AboutCity: String
        set(value) {
            field = value
        }
        get(): String{
            return field
        }

    var Key: String
        set(value) {
            field = value
        }
        get(): String{
            return field
        }
    var State: String
        set(value) {
            field = value
        }
        get() {
            return field
        }
    constructor(cityName: String, aboutCity: String, state: String, key: String ){
        CityName = cityName
        AboutCity = aboutCity
        Key = key
        State = state
    }

}