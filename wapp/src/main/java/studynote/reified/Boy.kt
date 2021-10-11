package studynote.reified

/**
 * Created by jsxiaoshui on 2021/8/9
 */
class Boy(private val name:String ,private val age:Int):Human() {
    override fun toString(): String {
        return "Boy(name='$name', age=$age)"
    }
}