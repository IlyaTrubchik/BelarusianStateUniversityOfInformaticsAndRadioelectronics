
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

public class LFSR {


    String register = "";
    String pathFrom = "";
    String pathTo="";
    long registerLong = 0;
    byte keyByte = 0;
    final int BufferCapacity = 3*1024*1024;
    // p(x) = x^39+x^4+1;
    public LFSR (String register)
    {
        this.pathFrom = "C:\\Users\\Ilya\\Desktop\\TI2.txt";
        this.pathTo = "C:\\Users\\Ilya\\Desktop\\TI21.txt";
        this.register =register;
    }
   /* private void getRegisterLong(){

        for(int i = 0;i<39;i++)
        {
            this.registerLong = this.registerLong + (long)(Math.pow(2,(double)38-i))*this.register.charAt(i);
        }
    }
    */

    public void cypherFile() throws IOException {
        this.registerLong =  Long.parseLong(this.register,2);
        SeekableByteChannel from = Files.newByteChannel(Path.of("C:\\Users\\Ilya\\Desktop\\coded.mp4"));
        SeekableByteChannel to = Files.newByteChannel(Path.of("C:\\Users\\Ilya\\Desktop\\decoded.mp4"),CREATE,WRITE);
        SeekableByteChannel toKey = Files.newByteChannel(Path.of("C:\\Users\\Ilya\\Desktop\\key.txt"),CREATE,WRITE);
        byte[] bufArray = new byte[BufferCapacity];
        ByteBuffer buf1 = ByteBuffer.wrap(bufArray);
        long curr = System.currentTimeMillis();
        while(from.read(buf1)>0)
        {
            byte[] keyArr = new byte[buf1.position()];
            for(int  i =0;i<buf1.position();i++)
            {
                bufArray[i] = (byte) (bufArray[i] ^ this.shift());
                keyArr[i] = this.keyByte;
            }
            toKey.write(ByteBuffer.wrap(keyArr));
            buf1.flip();
            to.write(buf1);
            buf1.clear();
        }
        System.out.println("\nTime:"+Long.toString(System.currentTimeMillis() - curr));
    }
    private byte shift(){
        byte result = 0;
        for(int i = 0;i<8;i++) {
            long highestBit = (this.registerLong & 0b0000000000000000000000000100000000000000000000000000000000000000L) >> 38;
            result = (byte) ((result<<1) + (byte)highestBit);
            long forthBit = (this.registerLong & 0b0000000000000000000000000000000000000000000000000000000000001000L) >> 3;
            this.registerLong = (this.registerLong << 1) + (highestBit ^ forthBit);
        }
        this.keyByte = result;
        return result;
    }
}
