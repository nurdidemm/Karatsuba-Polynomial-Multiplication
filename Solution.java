import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import java.util.Arrays;


class Result {

    /*
     * Complete the 'smellCosmos' function below.
     *
     * The function is expected to return a LONG_INTEGER_ARRAY.
     * The function accepts following parameters:
     *  1. LONG_INTEGER_ARRAY a
     *  2. LONG_INTEGER_ARRAY b
     */
    
    
    public static List<Long> smellCosmos(List<Long> a, List<Long> b) {
        long[] arr1 = a.stream().mapToLong(l -> l).toArray();
        long[] arr2 = b.stream().mapToLong(l -> l).toArray();
        
        long[] arrayResult = Karatsuba(arr1, arr2);
        
        List<Long> result = new ArrayList();
        
        for(int j=0; j < arrayResult.length -1; j++) {
            result.add(arrayResult[j]);
        }
        return result;

    }
    
    public static long[] Karatsuba(long[] a, long[] b) {
            int n = a.length;
            int k = n/2;
            
            //grand product
            long[] z = new long[2*n];
    
            //base case
            if (n<=32) {
                z = multiplyPoly(a, b, a.length);
                return z;
            }
            //recursive calls
            else {
                long[] aL = new long[k];
                long[] aR = new long[k];
                long[] bL = new long[k];
                long[] bR = new long[k];
                
                long[] aLaR = new long[k];
                long[] bLbR = new long[k];
                

                for (int i=0; i < k; ++i){
                    aL[i] = a[i];
                    aR[i] = a[i+k];
                    bL[i] = b[i];
                    bR[i] = b[i+k];

                    aLaR[i] = mod(aL[i] + aR[i]);
                    bLbR[i] = mod(bL[i] + bR[i]);
                }
                
//                 System.out.println("");
    
//                 System.out.print("aL: ");
//                 for(int i=0; i < aL.length; i++) {
//                     System.out.print(aL[i] + " ");
//                 }
//                 System.out.println("");

//                 System.out.print("aR: ");
//                 for(int i=0; i < aR.length; i++) {
//                     System.out.print(aR[i] + " ");
//                 }
//                 System.out.println("");

//                 System.out.print("bL: ");
//                 for(int i=0; i < bL.length; i++) {
//                     System.out.print(bL[i] + " ");
//                 }
//                 System.out.println("");

//                 System.out.print("bR: ");
//                 for(int i=0; i < bR.length; i++) {
//                     System.out.print(bR[i] + " ");
//                 }
//                 System.out.println("");
    
                long[] p1 = Karatsuba(aL, bL);
                long[] p2 = Karatsuba(aR, bR);
                long[] p3 = Karatsuba(aLaR, bLbR);
                
                //assemble middle 
                long[] productMiddle = new long[n];
                

                for (int j = 0; j < n-1; ++j) {
                    productMiddle[j] = mod(p3[j] - p1[j] - p2[j]);
                }

                //assemble total product
                for (int j = 0, middleOffset = k; j < n-1; ++j) {
                    z[j] = mod(z[j] + p1[j]);
                    z[j + n] = mod(z[j + n] + p2[j]);
                    z[j + middleOffset] = mod(z[j + middleOffset] + productMiddle[j]);
                }          
                return z;
            }
    }
    
    
    public static long[] multiplyPoly(long[] A, long[] B, int n){ 
        long[] prod = new long[n*2]; 
  
        for (int i = 0; i < n*2; i++){ 
            prod[i] = 0; 
        }
        for (int i = 0; i < n; i++){ 
            for (int j = 0; j < n; j++){ 
                prod[i + j] = mod(prod[i + j] + A[i] * B[j]); 
            } 
        } 
        return prod; 
    } 
    
    
    public static long[] arrayAddition(long[] x, long[] y) {
    
        long[] newArr = new long[x.length];
        for (int i=0; i < newArr.length; i++) {
            newArr[i] = mod(x[i] + y[i]);
        }
        return newArr;
    }
    
    public static long[] arraySubtract(long[] x, long[] y) {
    
        long[] newArr = new long[x.length];
        for (int i=0; i < newArr.length; i++) {
            newArr[i] = mod(x[i] - y[i]);
        }
        return newArr;
    }
    
    public static long mod(long L) {
        if (L >= 1000000009) {
            return L % 1000000009;
        } 
        else if (L < 0) {
            return ((L % 1000000009) + 1000000009) % 1000000009;
        }
        else {
            return L;
        }
    }
    
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<Long> a = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
            .map(Long::parseLong)
            .collect(toList());

        List<Long> b = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
            .map(Long::parseLong)
            .collect(toList());

        List<Long> result = Result.smellCosmos(a, b);

        bufferedWriter.write(
            result.stream()
                .map(Object::toString)
                .collect(joining(" "))
            + "\n"
        );

        bufferedReader.close();
        bufferedWriter.close();
    }
}