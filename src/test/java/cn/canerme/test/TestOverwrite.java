package cn.canerme.test;

/**
 * UTF-8
 * Created by czy  Time : 2021/1/14 16:46
 *
 * @version 1.0
 */
public class TestOverwrite {


    public static void main(String[] args) {
        new sun().say("s","s");
    }
}
class fa{
    Object say(String a,String b){
        System.out.println("父类方法");
        return null;
    }
}

class sun extends fa{
    @Override
    String say(String a, String b) {
        System.out.println("子类方法");
        return null;
    }
//    String  say(String a,Object b){
//
//        return null;
//    }
}
