package com.gorup79.vlc.dto;

import org.springframework.stereotype.Component;

@Component
public class JwtDTO {

    private String jwt;

    public JwtDTO(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public void setJwt() {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("JwtDTO{");
        sb.append("jwt=").append(jwt);
        sb.append('}');
        return sb.toString();
    }

}
