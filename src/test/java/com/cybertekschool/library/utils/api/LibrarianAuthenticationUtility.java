package com.cybertekschool.library.utils.api;


import com.cybertekschool.library.utils.common.Encoder;
import com.cybertekschool.library.utils.common.Environment;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class LibrarianAuthenticationUtility implements AuthenticationUtility {
    private static Response response;
    private String token;
    private String redirectUrl;

    @Override
    public Response getLoginResponse() {
        if (response == null) {
            String username = Environment.getProperty("librarian_email");
            String password = Environment.getProperty("librarian_password");
            password = Encoder.decrypt(password);
            response = given().
                    formParam("email", username).
                    formParam("password", password).
                    log().all().
                when().
                    post(Environment.getProperty("libraryurl")+Endpoints.LOGIN).prettyPeek();
            response.then().statusCode(200);
        }
        return response;
    }

    @Override
    public String getToken() {
        if (token == null) {
            token = getLoginResponse().jsonPath().getString("token");
        }
        return token;
    }

    @Override
    public String getRedirectUrl() {
        if (redirectUrl == null) {
            redirectUrl = getLoginResponse().jsonPath().getString("redirect_uri");
        }
        return redirectUrl;
    }


    public Response getResponseForUsers(String username, String password) {
            response = given().
                    formParam("email", username).
                    formParam("password", password).
                    log().all().
                    when().
                    post(Environment.getProperty("libraryurl")+Endpoints.LOGIN).prettyPeek();
        return response;
    }
}
