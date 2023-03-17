package studynote.extension

/**
 * Created by jsxiaoshui on 2021/8/8
 */

fun <T> Iterable<T>.randomTake():T=this.shuffled().first()
