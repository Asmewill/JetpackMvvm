package studynote.fanxing;

import java.util.ArrayList;
import java.util.List;

import studynote.fanxing.obj.Person;
import studynote.fanxing.obj.Student;
import studynote.fanxing.obj.Worker;

/**
 * Created by jsxiaoshui on 2021/7/31
 */
public class TestFx {

    private Person person=new Person();
    private Student student=new Student();
    private Worker worker=new Worker();
    void test(){
        List<? extends Person> list= new ArrayList<Student>();
//        //不能修改
//        list.add(worker);
//        list.add(student);
//        //能获取
//        Person person1= list.get(0);
//        Person person2=list.get(1);
        List<? super Student> list1=new ArrayList<Person>();

       // list1.add(worker);
        list1.add(student);
       // list1.add(person);

        Person person1= list.get(0);
        Person person2=list.get(1);
    }
}
