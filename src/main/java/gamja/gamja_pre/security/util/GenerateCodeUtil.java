package gamja.gamja_pre.security.util;

import lombok.NoArgsConstructor;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@NoArgsConstructor
public final class GenerateCodeUtil {
    public static String generateCode() {
        String code;

        try {
            SecureRandom random = SecureRandom.getInstanceStrong(); // 임의의 int 값을 생성하는 SecureRandom 인스턴스 생성
            int c = random.nextInt(9000) + 1000;
            code = String.valueOf(c);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Problem when generating the random code.");
        }
        return code;
    }
}
