package com.razor0719.common.auth;

import java.io.Serializable;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author baoyl
 * @created 2019/4/1
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthHolder {

    public static boolean isLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    @SuppressWarnings("unchecked")
    public static <U extends AuthUser<K>, K extends Serializable> U currentUser() {
        if (isLogin()) {
            return (U) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <K extends Serializable> K currentUserId() {
        AuthUser<K> user = (AuthUser<K>) currentUser();
        return user == null ? null : user.getId();
    }
}
