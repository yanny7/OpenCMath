## OpenCMath: Complex and matrix math library

Complex math with matrix support, for better performance used object pool

### Example:

Basic usage:
```java
import com.opencmath.BaseNumber;

public class Main {
    public static void main(String[] args) {
        BaseNumber a = BaseNumber.getReal(2.5); // construct real number
        BaseNumber b = BaseNumber.getComplex(2.5, 0.5); // construct complex number
        
        BaseNumber result = a.add(b); // sum "a" and "b" and store result into "result"
                                      // both "a" and "b" are returned there into pool
        
        System.out.println(result); // prints [5.0+0.5i]
        BaseNumber.put(result); // return object back to pool
    }
}
```
Always put result of calculation into new variable **c = a.add(b)** or use **a = a.add(b)**.
Otherwise you lose pointer to object from pool what cause memory leak.

### Supported operations:
* Addition, subtraction, multiplication, division
* Trigonometry: sine, cosine, tangent, cotangent, secant, cosecant (inverse, hyperbolic, inverse hyperbolic) and conversion to/from radians, gradians and degrees
* Natural logarithm, logarithm with base, exponential function, argument function, absolute value
* Power function, root function, square root, factorial
* Binary operations and, or, xor, not, shift left, shift right (**only integer type**)
* Matrix functions: determinant, rank, trace, transpose, adjugate, inverse

All operations except binary support complex numbers and matrices.
