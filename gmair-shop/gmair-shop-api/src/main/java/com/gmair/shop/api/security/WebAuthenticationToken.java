

package com.gmair.shop.api.security;

import com.gmair.shop.security.token.MyAuthenticationToken;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * H5端Token
 */
@NoArgsConstructor
public class WebAuthenticationToken extends MyAuthenticationToken {


    public WebAuthenticationToken(UserDetails principal, Object credentials) {
        super(principal, credentials, principal.getAuthorities());
    }
}
