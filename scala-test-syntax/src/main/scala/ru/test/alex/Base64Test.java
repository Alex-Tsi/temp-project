package ru.test.alex;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

public class Base64Test {
    public static void main(String[] args) throws URISyntaxException, IOException {
        byte[] png = Files.readAllBytes(new File(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("Syerra_K_Izuchaem_Java_Head_Firts_Java__2012.pdf")).toURI()).toPath());
//        System.out.println(new String(Base64.getEncoder().encode(png)));
//        System.out.println(png.length / 1024 / 1024);
//        System.out.println(Base64.getEncoder().encode(png).length / 1024 / 1024);
      /*  System.out.println(new String(Base64.getDecoder().decode("LS0tLS0tLS0tLSBGb3J3YXJkZWQgbWVzc2FnZSAtLS0tLS0tLS0NCtCe0YI6IExhbW9kYSBJbmZv" +
                "IDxtYWlsQG5vdGlmaWNhdGlvbi5sYW1vZGEucnU+DQpEYXRlOiDQv9GCLCAyMCDQvNCw0Y8gMjAy" +
                "MiDQsy4g0LIgMDg6MzkNClN1YmplY3Q6INCQ0LvQtdC60YEsINC80Ysg0L/RgNC40LLQtdC30LvQ" +
                "uCDQstCw0Ygg0LfQsNC60LDQtyDihJZSVTIyMDUxNy05NjM3Mzkg0LIg0L/Rg9C90LrRgiDRgdCw" +
                "0LzQvtCy0YvQstC+0LfQsA0KVG86IDxzZXZlbmxhbHpAZ21haWwuY29tPg0KDQoNCtCe0L3Qu9Cw" +
                "0LnQvS3QstC10YDRgdC40Y8NCjxodHRwOi8vbm90aWZpY2F0aW9uLmxhbW9kYS5ydS9jL2RjP3Q9" +
                "b2wmcD0xNzQ0LlFaMlNaMjVPVVcyTTRJTkRYQkpBLlRKSEJIN00zMDk+DQrQktCw0YgNCtC30LDQ" +
                "utCw0Lcg0L3QsNGF0L7QtNC40YLRgdGPINCyINC/0YPQvdC60YLQtSDRgdCw0LzQvtCy0YvQstC+" +
                "0LfQsA0KDQpbaW1hZ2U6IExhbW9kYSDigJMg0JzQvtC00LAg0YEg0LTQvtGB0YLQsNCy0LrQvtC5" +
                "IV0NCjxodHRwczovL3d3dy5sYW1vZGEucnUvP3V0bV9zb3VyY2U9bmwmdXRtX21lZGl1bT1lbSZ1" +
                "dG1fY2FtcGFpZ249MjAyMjA1MjBfMTIwODQxX0FVX2Fycml2YWxfdG9fcGlja191cF9wb2ludCZh" +
                "ZGp1c3RfY2FtcGFpZ249MjAyMl8wNV8yMF8xMjA4NDFfQVVfYXJyaXZhbF90b19waWNrX3VwX3Bv" +
                "aW50JmFkanVzdF90PTd3YW04emZfaTRpaTd5bSZ1dG1fbm9vdmVycmlkZT0xPg0K0JLQsNGIINC3" +
                "0LDQutCw0Lcg0L3QsNGF0L7QtNC40YLRgdGPINC90LAg0L/Rg9C90LrRgtC1INGB0LDQvNC+0LLR" +
                "i9Cy0L7Qt9CwDQrQl9C00YDQsNCy0YHRgtCy0YPQudGC0LUsINCQ0LvQtdC60YEhDQrQnNGLINC/" +
                "0YDQuNCy0LXQt9C70Lgg0LLQsNGIINC30LDQutCw0Lcg4oSWUlUyMjA1MTctOTYzNzM5DQo8aHR0" +
                "cHM6Ly93d3cubGFtb2RhLnJ1L3NhbGVzL29yZGVyL3ZpZXcvb3JkZXJfaWQvUlUyMjA1MTctOTYz" +
                "NzM5Lz91dG1fc291cmNlPW5sJnV0bV9tZWRpdW09ZW0mdXRtX2NhbXBhaWduPTIwMjJfMDVfMjBf" +
                "MTIwODQxX0FVX2Fycml2YWxfdG9fcGlja191cF9wb2ludCZhZGp1c3RfY2FtcGFpZ249MjAyMl8w" +
                "NV8yMF8xMjA4NDFfQVVfYXJyaXZhbF90b19waWNrX3VwX3BvaW50JmFkanVzdF90PTd3YW04emZf" +
                "aTRpaTd5bSZ1dG1fbm9vdmVycmlkZT0xPg0K0LIg0L/Rg9C90LrRgiDRgdCw0LzQvtCy0YvQstC+" +
                "0LfQsCDQvdCwINCa0LvRjtGH0LXQstCw0Y8sIDYg0LoxICjQvC4g0LwuINCQ0LvQvNCwLSDQkNGC" +
                "0LjQvdGB0LrQsNGPKQ0K0JzRiyDQttC00LXQvCDQstCw0YEg0YEg0J/QvS3QktGBIDEwOjAwLTIx" +
                "OjAwLg0K0JLQviDQvNC90L7Qs9C40YUg0YDQtdCz0LjQvtC90LDRhSDQv9GA0L7QtNC+0LvQttCw" +
                "0Y7RgiDQtNC10LnRgdGC0LLQvtCy0LDRgtGMINC60LDRgNCw0L3RgtC40L3QvdGL0LUg0L7Qs9GA" +
                "0LDQvdC40YfQtdC90LjRjzogKtC/0L7QttCw0LvRg9C50YHRgtCwLA0K0L3QtSDQt9Cw0LHRg9C0" +
                "0YzRgtC1INCy0LfRj9GC0Ywg0YEg0YHQvtCx0L7QuSDQvNCw0YHQutGDINC00LvRjyDQv9C+0YHQ" +
                "tdGJ0LXQvdC40Y8g0L/Rg9C90LrRgtCwINGB0LDQvNC+0LLRi9Cy0L7Qt9CwLioqDQoNCirQodGA" +
                "0LXQtNGB0YLQstCwINC40L3QtNC40LLQuNC00YPQsNC70YzQvdC+0Lkg0LfQsNGJ0LjRgtGLINC9" +
                "0LUg0L7QsdGP0LfQsNGC0LXQu9GM0L3RiyDQtNC70Y8g0L/QvtGB0LXRidC10L3QuNGPINC/0YPQ" +
                "vdC60YLQvtCyDQrRgdCw0LzQvtCy0YvQstC+0LfQsCDQsiDQnNC+0YHQutCy0LUg0Lgg0JzQvtGB" +
                "0LrQvtCy0YHQutC+0Lkg0L7QsdC70LDRgdGC0LguDQrQldGB0LvQuCDRgyDQstCw0YEg0L7RgdGC" +
                "0LDQu9C40YHRjCDQstC+0L/RgNC+0YHRiywg0L/QvtGB0LXRgtC40YLQtSDQvdCw0Ygg0YbQtdC9" +
                "0YLRgCDQv9C+0LTQtNC10YDQttC60LgNCjxodHRwczovL3d3dy5sYW1vZGEucnUvaGVscC8/dXRt" +
                "X3NvdXJjZT1ubCZ1dG1fbWVkaXVtPWVtJnV0bV9jYW1wYWlnbj0yMDIyXzA1XzIwXzEyMDg0MV9B" +
                "VV9hcnJpdmFsX3RvX3BpY2tfdXBfcG9pbnQmYWRqdXN0X2NhbXBhaWduPTIwMjJfMDVfMjBfMTIw" +
                "ODQxX0FVX2Fycml2YWxfdG9fcGlja191cF9wb2ludCZhZGp1c3RfdD03d2FtOHpmX2k0aWk3eW0m" +
                "dXRtX25vb3ZlcnJpZGU9MT4NCtCS0LDRiNCwIExhbW9kYQ0K0J/QvtC20LDQu9GD0LnRgdGC0LAs" +
                "INC90LUg0L7RgtCy0LXRh9Cw0LnRgtC1INC90LAg0LTQsNC90L3QvtC1INC/0LjRgdGM0LzQviwg" +
                "0L7QvdC+INC+0YLQv9GA0LDQstC70LXQvdC+INCw0LLRgtC+0LzQsNGC0LjRh9C10YHQutC4Lg0K" +
                "DQoNCjxodHRwczovL3d3dy5sYW1vZGEucnUvaGVscC8/dXRtX3NvdXJjZT1ubCZ1dG1fbWVkaXVt" +
                "PWVtJnV0bV9jYW1wYWlnbj0yMDIyXzA1XzIwXzEyMDg0MV9BVV9hcnJpdmFsX3RvX3BpY2tfdXBf" +
                "cG9pbnQmYWRqdXN0X2NhbXBhaWduPTIwMjJfMDVfMjBfMTIwODQxX0FVX2Fycml2YWxfdG9fcGlj" +
                "a191cF9wb2ludCZhZGp1c3RfdD03d2FtOHpmX2k0aWk3eW0mdXRtX25vb3ZlcnJpZGU9MT4NCg0K" +
                "0J/QtdGA0LXQudGC0Lgg0LIg0KbQtdC90YLRgCDQv9C+0LTQtNC10YDQttC60LggbGFtb2RhDQo8" +
                "aHR0cHM6Ly93d3cubGFtb2RhLnJ1L2hlbHAvP3V0bV9zb3VyY2U9bmwmdXRtX21lZGl1bT1lbSZ1" +
                "dG1fY2FtcGFpZ249MjAyMl8wNV8yMF8xMjA4NDFfQVVfYXJyaXZhbF90b19waWNrX3VwX3BvaW50" +
                "JmFkanVzdF9jYW1wYWlnbj0yMDIyXzA1XzIwXzEyMDg0MV9BVV9hcnJpdmFsX3RvX3BpY2tfdXBf" +
                "cG9pbnQmYWRqdXN0X3Q9N3dhbTh6Zl9pNGlpN3ltJnV0bV9ub292ZXJyaWRlPTE+DQo=")));*/

        System.out.println(new String(Base64.getDecoder().decode("5vMbyqxGkddhh69sIkKp9o8RZ/cHVAHjLU58szy9hXFmGkiudDwHBjzHxkDc8hmhDokxS79pR1wUYmIrM9OjQ/YkQ1IMHz77vVlg6dEygdSaTcDh7oSsz8ffWcueAffNLpTPu/E2K15QNi9w20rrUoRm+t0aqlCASebvvz3ZR913S/VT2a3Nn3P3dZIGYomRLN6ansyAY39Ee+Mn2cHkEA==")));
    }
}
