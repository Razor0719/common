package com.razor0719.common.auth;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author baoyl
 * @created 2019/3/30
 */
public interface AuthUser<K extends Serializable> extends UserDetails {

    /**
     * 认证用户id
     * @return K
     */
    K getId();

    /**
     * 设置权限
     * @param authorities
     */
    void setAuthorities(Collection<? extends GrantedAuthority> authorities);

    /**
     * 是否拥有该权限
     * @param authority
     * @return boolean
     */
    boolean hasAuthority(GrantedAuthority authority);

    /**
     * 拥有任意一个权限
     * @param authorities
     * @return boolean
     */
    boolean hasAnyAuthority(Collection<? extends GrantedAuthority> authorities);

    /**
     * 拥有所有权限
     * @param authorities
     * @return boolean
     */
    boolean hasAllAuthority(Collection<? extends GrantedAuthority> authorities);
}
