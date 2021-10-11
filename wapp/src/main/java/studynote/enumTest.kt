package studynote

/**
 * Created by jsxiaoshui on 2021/8/9
 */
//枚举类
enum class LicenseStatus{
    NOLICENSE,
    LEARNING,
    HASLICENSE;
}
class Driver(var status:LicenseStatus){
    fun checkLicense():String{
        return when(status){
            LicenseStatus.NOLICENSE->"无驾照"
            LicenseStatus.LEARNING->"在学习"
            LicenseStatus.HASLICENSE->"有驾照"
        }
    }
}

//密封类，相对与枚举类来讲更灵活一些，比如说要传licenseId的时候
sealed class LicenseStatus2{
    object NoLicense:LicenseStatus2()
    object Learning:LicenseStatus2()
    class HasLicense(val licenseId:String):LicenseStatus2()
}

//is as  in   !is
//as的用法
//1，类型转换。
//2，重取别名。
//3, as? 表示安全类型转换。
class Driver2(var status:LicenseStatus2){
     fun checkLicense():String{
         //测试  !is 的用法
         if(status !is LicenseStatus2.Learning){
              println("no license")
         }
       return  when(status){
           is  LicenseStatus2.NoLicense->{ "无驾照"}
           is  LicenseStatus2.Learning->{"在学习"}
           is  LicenseStatus2.HasLicense->{"有驾照,驾照编号:${(this.status as LicenseStatus2.HasLicense).licenseId}"}
         }
     }

}

fun main() {
    println(Driver(LicenseStatus.HASLICENSE).checkLicense())

    println(Driver2(LicenseStatus2.HasLicense("6003")).checkLicense())


    println(Driver2(LicenseStatus2.Learning).checkLicense())
}
