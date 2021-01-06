import com.cs.security.SecurityExample;
import org.junit.jupiter.api.Test;

public class SecurityTest {

    @Test
    public void testExample23(){
        SecurityExample.example23();

    }
    @Test
    public void testEncoder(){
        SecurityExample.bCryptPasswordEncoder();
    }
}
