package nb.hanquoc.web;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        // Configure in-memory user details for authentication
        auth.inMemoryAuthentication()
                .withUser("eunmi").password(passwordEncoder.encode("8562")).roles("USER").and()
                .withUser("jami").password(passwordEncoder.encode("1695")).roles("USER").and()
                .withUser("xc").password(passwordEncoder.encode("6902")).roles("USER").and()
                .withUser("mdh").password(passwordEncoder.encode("7008")).roles("USER").and()
                .withUser("mdt").password(passwordEncoder.encode("5755")).roles("USER").and()
                .withUser("danah").password(passwordEncoder.encode("4701")).roles("USER").and()
                .withUser("keb").password(passwordEncoder.encode("6176")).roles("USER").and()
                .withUser("hkg").password(passwordEncoder.encode("2337")).roles("USER").and()
                .withUser("am").password(passwordEncoder.encode("2369")).roles("USER").and()
                .withUser("leehwa").password(passwordEncoder.encode("1172")).roles("USER").and()
                .withUser("kilc").password(passwordEncoder.encode("2168")).roles("USER").and()
                .withUser("kn").password(passwordEncoder.encode("1691")).roles("USER").and()
                .withUser("jeil").password(passwordEncoder.encode("9546")).roles("USER").and()
                .withUser("vikos").password(passwordEncoder.encode("4933")).roles("USER").and()
                .withUser("busan").password(passwordEncoder.encode("7293")).roles("USER").and()
                .withUser("gwangju").password(passwordEncoder.encode("5038")).roles("USER").and()
                .withUser("nvseoul").password(passwordEncoder.encode("6447")).roles("USER").and()
                .withUser("jk").password(passwordEncoder.encode("8517")).roles("USER").and()
                .withUser("wl").password(passwordEncoder.encode("3643")).roles("USER").and()
                .withUser("sw").password(passwordEncoder.encode("5144")).roles("USER").and()
                .withUser("hv").password(passwordEncoder.encode("5549")).roles("USER").and()
                .withUser("np").password(passwordEncoder.encode("9934")).roles("USER").and()
                .withUser("yeohae").password(passwordEncoder.encode("5528")).roles("USER").and()
                .withUser("hoanglong").password(passwordEncoder.encode("7216")).roles("USER").and()
                .withUser("sen").password(passwordEncoder.encode("seoul")).roles("USER").and()
                .withUser("thu").password(passwordEncoder.encode("seoul")).roles("USER").and()
                .withUser("tuyen").password(passwordEncoder.encode("seoul")).roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/css/**", "/download/**", "/address", "/ajax/**").permitAll() // Cho phép tất cả mọi người truy cập vào 2 địa chỉ này                                                                                
                .anyRequest().authenticated() // Tất cả các request khác đều cần phải xác thực mới được truy cập
                .and()
                .formLogin() // Cho phép người dùng xác thực bằng form login
                .defaultSuccessUrl("/index")
                .permitAll() // Tất cả đều được truy cập vào địa chỉ này
                .and()
                .logout() // Cho phép logout
                .permitAll();
    }
}
