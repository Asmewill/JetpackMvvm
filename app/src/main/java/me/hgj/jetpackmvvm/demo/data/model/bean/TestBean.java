package me.hgj.jetpackmvvm.demo.data.model.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import me.hgj.jetpackmvvm.demo.BR;

/**
 * Created by Owen on 2025/4/17
 * 双向绑定实现声明式UI
 */
public class TestBean extends BaseObservable {

    private String name;
    private String age;
    @Bindable
    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
        notifyPropertyChanged(BR.age);
    }

    @Bindable
    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name); // 触发UI更新
    }
}
