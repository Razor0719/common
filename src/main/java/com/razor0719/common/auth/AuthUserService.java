package com.razor0719.common.auth;

import java.io.Serializable;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author baoyl
 * @created 2019/4/1
 */
interface AuthUserService<U extends AuthUser<K>, K extends Serializable> extends UserDetailsService {
    /**
     * 根据id获取用户
     *
     * @param id
     * @return U
     */
    U loadUserById(K id);

    /**
     * 根据用户名获取用户
     *
     * @param username
     * @return U
     * @throws UsernameNotFoundException
     */
    @Override
    U loadUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * 根据id获取权限
     *
     * @param id
     * @return List<GrantedAuthority>
     */
    List<GrantedAuthority> loadAuthoritiesById(K id);
}
