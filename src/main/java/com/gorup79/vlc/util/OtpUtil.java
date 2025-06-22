package com.gorup79.vlc.util;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class OtpUtil {

    public static String generateOtp(int length) {
        String numbers = "0123456789";
        Random random = new Random();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < length; i++) {
            otp.append(numbers.charAt(random.nextInt(numbers.length())));
        }

        return otp.toString();
    }
}
