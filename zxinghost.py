#!/usr/bin/python
import os, subprocess, datetime

class zxing_host:
    __libs__ = ["javase.jar", "core.jar"]
    __java_host__ = "zxingHost"
    def __init__(self, libs_loc = '.'):    
        libs = [libs_loc + "/" + l for l in self.__libs__]
        libs.insert(0, ".")
        cmd = ["java", "-cp", os.pathsep.join(libs), self.__java_host__]
        try:
            self.__zxing_process = subprocess.Popen(cmd,
                                                    shell=True,
                                                    stdin=subprocess.PIPE,
                                                    stdout=subprocess.PIPE,
                                                    universal_newlines=True)
        except Exception as error:
            ptint(error)
            raise error
        
    def __del__(self):
        self.close()
        
    def decodeBase64(self, base64):
        if self.__zxing_process:
            cmd = 'base64 {0}\n'.format(base64)
            return self.__sendCommand(cmd)

    def decodeFile(self, file):
        if self.__zxing_process:
            cmd = 'file {0}\n'.format(file)
            return self.__sendCommand(cmd)

    def encode(self, text, file):
        if self.__zxing_process:
            cmd = 'create {0} {1}\n'.format(text, file)
            return self.__sendCommand(cmd)
        
    def close(self):
        if self.__zxing_process:
            self.__zxing_process.communicate(input='q\n')
            del(self.__zxing_process)


    def __sendCommand(self, command):
        self.__zxing_process.stdin.write(command)
        self.__zxing_process.stdin.flush()
        out = self.__zxing_process.stdout.readline()
        if hasattr(out, 'strip'): out = out.strip(os.linesep)
        return out

        
if __name__ == '__main__':
    zxing = zxing_host('zxing3.2.1')
    for i in range(50):
        print(str(datetime.datetime.now().time()))
        out = zxing.encode('"Welcome to Python.org"', 'test.png')
        print("{0} - {1}".format(datetime.datetime.now().time(), out))
        out = zxing.decodeBase64(
            'iVBORw0KGgoAAAANSUhEUgAAAMgAAADIAQAAAACFI5MzAAAA10lEQVR42u3XOw7DIAwGYDNxDG6ahptyDKa4fmV'\
            'I2sz8kYyiiuZbLGwMIX4alJKS8g6ZJKNM+sy267QhiU47D5F+/sWRnVqf/kj4iHJURhUqjCiWbY5fLPFdoov6f/'\
            '8slBijPPWdhSJRaw0228TXbCMIyRbR1760QGJtr1jIW+WDkCRe6IkhscscS2Ql65Cn3HfwavFh9bhbSQKJF6DlP'\
            'FoLkMQtQHyrZ4+BET3N2GtQco4nfkMBFe15eklhLNFsW2vRSvy5HyyVOChUblEvl/yeS0l5qXwBYfaE7gyTgqsA'\
            'AAAASUVORK5CYII=')
        print("{0} - {1}".format(datetime.datetime.now().time(), out))
        out = zxing.decodeFile('test.png')
        print("{0} - {1}".format(datetime.datetime.now().time(), out))
    del(zxing)
