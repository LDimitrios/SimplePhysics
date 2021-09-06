package other

inline fun <A, B> Pair<A, A>.map(transform: (A) -> B) = transform(first) to transform(second)