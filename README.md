#Compile Java zxing host:#

(Windows) : `javac -cp zxing3.2.1/core.jar;zxing3.2.1/javase.jar zxingHost.java`
(Linux) : `javac -cp zxing3.2.1/core.jar:zxing3.2.1/javase.jar zxingHost.java`

#Run and test in console by Java:#

(Windows) : `java -cp ./;./zxing3.2.1/* zxingHost`
(Linux) : `java -cp ./:./zxing3.2.1/core.jar:./zxing3.2.1/javase.jar zxingHost`

## Please note: zxing3.2.1 does not supoort in JDK 6. Please use zxing2.2 in JDK 6. ##

#Support commands:#

* "createqrcode"  "create"  "qr": Create and encode a QRCode from one string. 
    example: create <string_content> <qrcode_image>
* "readfilecode"  "readfile"  "file": Decode from a code image file.
    example: file <code_image>
* "readbase64code"  "readbase64"  "base64": Decode from base64 string of one code image.
    example: base64 <base64_of_code_image>
* "exit"  "quit"  "q": Exit program

#Others#
* `zxing_winformhost.exe` is a .Net Winform testing program, that will launch a Java zxing host in background and using standard I/O to decode the code image (in base64 format).
* `zxinghost.py` is a python wrapper, that contains one class can launch zxing Java host in background and transfer data via standard I/O to background process.
