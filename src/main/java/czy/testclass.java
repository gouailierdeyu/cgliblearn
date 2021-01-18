package czy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.accessibility.AccessibleExtendedComponent;
import java.lang.reflect.Field;

/**
 * UTF-8
 * Created by czy  Time : 2021/1/8 20:51
 *
 * @version 1.0
 */
public class testclass {
    public static void main(String[] args) throws IllegalAccessException, JsonProcessingException {
        Object o=new reflectczy(12,5);
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(o));
        for (Field declaredField : o.getClass().getDeclaredFields()) {
            declaredField.setAccessible(true);
            System.out.println(declaredField.getName());
            System.out.println(declaredField.get(o));
        }
    }
}

class reflectczy{

    private int a;
    private int b;

    public reflectczy(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }
}
