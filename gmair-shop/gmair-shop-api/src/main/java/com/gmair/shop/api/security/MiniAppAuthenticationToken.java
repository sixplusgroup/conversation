

package com.gmair.shop.api.security;

import com.gmair.shop.security.token.MyAuthenticationToken;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 二维码Token
 */
@NoArgsConstructor
public class MiniAppAuthenticationToken extends MyAuthenticationToken {


    public MiniAppAuthenticationToken(UserDetails principal, Object credentials) {
        super(principal, credentials, principal.getAuthorities());
    }
}
