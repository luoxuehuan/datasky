package com.hulb.study.javastudy;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by hulb on 17/3/15.
 *
 *
 * NIO学习笔记
 *
 * 1.NIO 介绍：
 *
 *      通道Channel
 *          Channel 有点象流。 数据可以从Channel读到Buffer中，也可以从Buffer 写到Channel中。
 *
 *      缓冲ByteBuffer
 *
 *      选择器 Selectors：
 *          Selector允许单线程处理多个 Channel。如果你的应用打开了多个连接（通道），但每个连接的流量都很低，使用Selector就会很方便。
 *
 *      通道从何而来：从InputStream或者OutputStream里来。
 *
 * 2.NIO 怎么读数据
 *
 *      2.1 写入数据到Buffer
 *      2.2 调用flip()方法
 *      2.3 从Buffer中读取数据
 *      2.4 调用clear() 方法 或者compact()方法
 *
 *      备注：
 *          当向buffer写入数据时，buffer会记录下写了多少数据。
 *          一旦要读取数据，需要通过flip()方法将Buffer从写模式切换到读模式。
 *          在读模式下，可以读取之前写入到buffer的所有数据。
 *
 *          一旦读完了所有的数据，就需要清空缓冲区，让它可以再次被写入。
 *          有两种方式能清空缓冲区：调用clear()或compact()方法。
 *          clear()方法会清空整个缓冲区。
 *          compact()方法只会清除已经读过的数据。
 *          任何未读的数据都被移到缓冲区的起始处，新写入的数据将放到缓冲区未读数据的后面。
 *
 * 3.NIO 怎么写数据
 *
 * 4.IO 和 NIO的对比
 *
 *      4.1 IO是面向流的，NIO是面向缓冲区的
 *      4.2 阻塞与非阻塞IO
 *      4.3 Java NIO的选择器允许一个单独的线程来监视多个输入通道
 *      4.4 nio做了内存映射，少了一次用户空间和系统空间之间的拷贝
 *      4.5 nio是异步，触发式的响应，非阻塞式的响应，充分利用了系统资源，主要是cpu
 *
 * 5.NIO在网络通信上的应用。
 *
 * 6.异步IO，没有阻塞的读写数据
 *  在代码进行read()调用时,代码会阻塞直至有可供读 取的数据。
 *  同样, write()调用将会阻塞直至数据能够写入。
 *  但异步I/O调用不会阻塞。
 *  使用异步I/O,您可以监听任何数量的通道上的事件,不用轮询,也不 用额外的线程。
 *
 *
 * 7. API调用
 *
 *      7.1 不是仅从一个InputStream逐字节读取，而是数据必须先读入缓冲区再处理。
 *
 */
public class NIOStudy {

    public static String inputFilePath = "/Users/hulb/work/github_space/datasky/data/txt/wordcount.txt";
    public static String ouputFilePath = "";

    public  static  void main(String[] arg) throws Exception{
        System.out.println("NIO Study start...");
        NIOStudy studyNIO = new NIOStudy();
        //studyNIO.firstNIOTest();
        studyNIO.nioRead();
    }

    @Test
    public void firstNIOTest() throws IOException {
        // 分配一个容量为10的新的 float 缓冲区
        FloatBuffer buffer = FloatBuffer.allocate(100);

        /**
         * 往缓冲区里面灌入100个随机数
         */
        for (int i = 0; i < buffer.capacity(); i++) {
            float f = (float) Math.sin((((float) i) / 10) * (2 * Math.PI));
            buffer.put(f);
        }

        // 反转此缓冲区-----从写入到 读取 需要 一次反转。
        buffer.flip();

        // 告知在当前位置和限制之间是否有元素
        while (buffer.hasRemaining()) {
            float f = buffer.get();
            System.out.println(f);
        }
    }


    /**
     * 使用IO读取指定文件的前1024个字节的内容。
     * @throws java.io.IOException IO异常。
     * */
    @Test
    public void ioRead( ) throws IOException {
        FileInputStream in = new FileInputStream(inputFilePath);
        long size = in.getChannel().size();

        System.out.println(size);//2623Integer.parseInt(String.valueOf(size)

        byte[] b = new byte[1024];
        int tag = in.read(b);
        /**
         * 如果要持续读取呢
         */
        System.out.println("read start ...");
        System.out.println(new String(b));
        System.out.println("read end ...");
    }


    /**
     * NIO以通道Channel和
     * 缓冲区Buffer为基础来
     * 实现面向块的IO数据处理
     *
     *
     * 实现：
     * 1 以1024 为buffer   从2644 读取 数据并打印（或写入另外一个文件）
     *
     *
     * @throws IOException
     */
    @Test
    public void nioRead() throws IOException {


        //第一步是获取通道。我们从 FileInputStream 获取通道:
        FileInputStream in = new FileInputStream(inputFilePath);
        long size = in.getChannel().size();
        System.out.println(size);//2623Integer.parseInt(String.valueOf(size)

        /**
         * 通道Channel是对原I/O包中的流的模拟
         */
        FileChannel channel = in.getChannel();
        /**
         *
         * 缓冲区Buffer实质上是一个容器对象
         * 发送给一个通道的所有对象都必须首先放到缓冲区中
         * 从通道 中读取的任何数据都要读到缓冲区中
         *
         * 分配1024字节的缓存
         */
        //下一步是创建缓冲区:
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.flip();

        /**
         * 通道read 1024 缓存。
         */
        //最后,需要将数据从通道读到缓冲区中
        channel.read(buffer);
        System.out.println("Read Start-------");

        /**
         * hasRemaining:
         */
        while(buffer.hasRemaining()){
            buffer.rewind();
            System.out.println(buffer.get());
        }
//        byte[] b = buffer.array();
//        System.out.println(new String(b));
    }

    @Test
    public void execute() throws IOException{
        //创建一个新的选择器
        Selector selector  = Selector.open();

        Integer[] ports = {10009,10010};

        //打开在每个端口上的监听，并想给定的选择器注册此通道接受客户端连接的IO事件
        for(int i = 0; i < ports.length;i++){

            // 打开服务器套接字通道
            ServerSocketChannel ssc = ServerSocketChannel.open();

            // 设置此通道为非阻塞模式
            ssc.configureBlocking(false);

            // 绑定到特定地址
            ServerSocket ss = ssc.socket();

            InetSocketAddress address = new InetSocketAddress(ports[i]);
            ss.bind(address);

            // 向给定的选择器注册此通道的接受连接事件
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Going to listen on"+ports[i]);
        }


        while (true){

            // 这个方法会阻塞,直到至少有一个已注册的事件发生。
            // 当一个或者更多的事件发生时,此方法将返回所发生的事件的数量。
            int num = selector.select();

            Set<SelectionKey> selectionKeys  = selector.selectedKeys();
            // 迭代所有的选择键,以处理特定的I/O事件。
            Iterator<SelectionKey> iter = selectionKeys.iterator();

            SocketChannel sc;

            while (iter.hasNext()){
                SelectionKey key = iter.next();

                if((key.readyOps()& SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT){
                    //接受服务器套接字撒很能够传入的新连接，并处理接受连接事件
                    ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
                    sc = ssc.accept();

                    //将新连接的套接字通道设置为非阻塞模式。
                    sc.configureBlocking(false);

                    //接受连接后，在此通道上新注册读取事件，以便接受数据。
                    sc.register(selector,SelectionKey.OP_READ);

                    //删除处理过的选择键。
                    iter.remove();

                    System.out.println("Got connection from "+ sc);
                }else if((key.readyOps()& SelectionKey.OP_READ)==SelectionKey.OP_READ){

                    //处理读取事件，读取套接字通道发来的数据。
                    sc = (SocketChannel)key.channel();

                    ByteBuffer echoBuffer  = ByteBuffer.allocate(48);

                    //读取数据
                    int bytesEchoed = 0;

                    while(true){
                        echoBuffer.clear();
                        int r = sc.read(echoBuffer);

                        if(r == -1){
                            break;
                        }

                        echoBuffer.flip();
                        sc.write(echoBuffer);
                        bytesEchoed +=r;
                    }

                    System.out.println("Echoes"+ bytesEchoed+"From "+sc);
                    //删除处理过的选择键
                    iter.remove();

                }
            }

        }


    }


    /**
     * 使用FileChannel读取数据到Buffer中的示例：
     * @throws Exception
     */
    @Test
    public void channelTest() throws Exception{

        RandomAccessFile aFile = new RandomAccessFile("data/nio-data.txt", "rw");
        FileChannel inChannel = aFile.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = inChannel.read(buf);
        while (bytesRead != -1) {

            System.out.println("Read " + bytesRead);
            /**
             * 注意 buf.flip() 的调用，首先读取数据到Buffer，然后反转Buffer,
             * 接着再从Buffer中读取数据。下一节会深入讲解Buffer的更多细节。
             */
            buf.flip();

            while(buf.hasRemaining()){
                System.out.print((char) buf.get());
            }

            buf.clear();
            bytesRead = inChannel.read(buf);
        }
        aFile.close();
    }


    /**
     *
     * 一个使用Buffer的例子：
     *
     * 写入数据到Buffer
     * 调用flip()方法
     * 从Buffer中读取数据
     * 调用clear()方法或者compact()方法
     * @throws Exception
     */
    @Test
    public void buffertest() throws Exception{

        RandomAccessFile aFile = new RandomAccessFile("data/txt/wordcount.txt", "rw");
        FileChannel inChannel = aFile.getChannel();

        //create buffer with capacity of 48 bytes
        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = inChannel.read(buf); //read into buffer.
        while (bytesRead != -1) {


            /**
             * flip方法将Buffer从写模式切换到读模式。
             * 调用flip()方法会将position设回0，并将limit设置成之前position的值。
             */
            buf.flip();  //make buffer ready for read

            while(buf.hasRemaining()){
                System.out.print((char) buf.get()); // read 1 byte at a time
            }

            buf.clear(); //make buffer ready for writing
            bytesRead = inChannel.read(buf);
        }
        aFile.close();


        /**
         * 注意buffer首先被插入到数组，然后再将数组作为channel.read() 的输入参数。
         * read()方法按照buffer在数组中的顺序将从channel中读取的数据写入到buffer，当一个buffer被写满后，channel紧接着向另一个buffer中写。

         Scattering Reads在移动下一个buffer前，必须填满当前的buffer，这也意味着它不适用于动态消息(译者注：消息大小不固定)。
         换句话说，如果存在消息头和消息体，消息头必须完成填充（例如 128byte），Scattering Reads才能正常工作。
         */
        ByteBuffer header = ByteBuffer.allocate(128);
        ByteBuffer body   = ByteBuffer.allocate(1024);

        ByteBuffer[] bufferArray = { header, body };

        inChannel.read(bufferArray);

        /**
         * buffers数组是write()方法的入参，write()方法会按照buffer在数组中的顺序，将数据写入到channel，
         * 注意只有position和limit之间的数据才会被写入。
         * 因此，如果一个buffer的容量为128byte，但是仅仅包含58byte的数据，那么这58byte的数据将被写入到channel中。
         * 因此与Scattering Reads相反，Gathering Writes能较好的处理动态消息。
         */
        //Gathering Writes是指数据从多个buffer写入到同一个channel
        inChannel.write(bufferArray);
    }


    /**
     * 直接从一个channel到另外一个channel
     * @throws Exception
     */
    @Test
    public void channeltochannel() throws Exception {
        RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");
        FileChannel fromChannel = fromFile.getChannel();
        RandomAccessFile toFile = new RandomAccessFile("toFile.txt", "rw");
        FileChannel toChannel = toFile.getChannel();

        long position = 0;
        long count = fromChannel.size();

        /**
         * 方法的输入参数position表示从position处开始向目标文件写入数据，count表示最多传输的字节数。
         * 如果源通道的剩余空间小于 count 个字节，则所传输的字节数要小于请求的字节数。
         *
         * FileChannel的transferFrom()方法可以将数据从源通道传输到FileChannel中
         */
        toChannel.transferFrom(fromChannel, position, count);

        /**
         * transferTo()方法将数据从FileChannel传输到其他的channel中
         */
        fromChannel.transferTo(position, count, toChannel);

    }


    /**
     * 仅用单个线程来处理多个Channels的好处是，只需要更少的线程来处理通道。
     * 事实上，可以只用一个线程处理所有的通道。对于操作系统来说，线程之间上下文切换的开销很大，而且每个线程都要占用系统的一些资源（如内存）。因此，使用的线程越少越好。

     * @throws Exception
     */
    @Test
    public void SelectorTest() throws Exception {

        //Selector的创建
        Selector selector = Selector.open();

        RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");
        //FileChannel channnel = fromFile.getChannel();

        ServerSocketChannel channnel = ServerSocketChannel.open();
        channnel.configureBlocking(false);
        SelectionKey key = channnel.register(selector,SelectionKey.OP_READ);

        while (true){
            int readyChannels = selector.select();
            if(readyChannels == 0 ) continue;
            Set selectedKeys = selector.selectedKeys();
            Iterator keyIterator = selectedKeys.iterator();
            while (keyIterator.hasNext()){
                SelectionKey keyi = (SelectionKey) keyIterator.next();
                if(keyi.isAcceptable()){
                    //a connection was accepted by a ServerSocketChannel
                }else if (keyi.isConnectable()){
                    //a connection was established with a remote server
                }else if (keyi.isReadable()){
                    //a channel is ready for reading
                }else if (keyi.isWritable()){
                    //a channel is ready for writing
                }
            }
        }


    }

    @Test
    public void FileChannel() throws Exception{
        RandomAccessFile aFile = new RandomAccessFile("data/nio-data","rw");
        FileChannel inChannel = aFile.getChannel();

        /**
         * 首先，分配一个Buffer。从FileChannel中读取的数据将被读到Buffer中。
         */
        ByteBuffer buffer = ByteBuffer.allocate(48);

        /**
         * 然后，调用FileChannel.read()方法。该方法将数据从FileChannel读取到Buffer中。
         * read()方法返回的int值表示了有多少字节被读到了Buffer中。
         * 如果返回-1，表示到了文件末尾。
         */
        int bytesReads = inChannel.read(buffer);

        String newData = "New String to write to file..."+System.currentTimeMillis();
        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        buf.put(newData.getBytes());

        buf.flip();

        /**
         * 注意FileChannel.write()是在while循环中调用的。
         * 因为无法保证write()方法一次能向FileChannel写入多少字节，
         * 因此需要重复调用write()方法，直到Buffer中已经没有尚未写入通道的字节。
         */
        while (buf.hasRemaining()){
            inChannel.write(buf);
        }

        long pos = inChannel.position();

        /**
         * 如果将位置设置在文件结束符之后，然后试图从文件通道中读取数据，读方法将返回-1 —— 文件结束标志。
         *
         *
         如果将位置设置在文件结束符之后，然后向通道中写数据，
         文件将撑大到当前位置并写入数据。这可能导致“文件空洞”，磁盘上物理文件中写入的数据间有空隙。
         */
        inChannel.position(pos+123);

        long fileSize = inChannel.size();

        /**
         * 这个例子截取文件的前1024个字节。
         */
        inChannel.truncate(1024);


        /**
         * FileChannel.force()方法将通道里尚未写入磁盘的数据强制写到磁盘上。
         * 出于性能方面的考虑，操作系统会将数据缓存在内存中，
         * 所以无法保证写入到FileChannel里的数据一定会即时写到磁盘上。
         * 要保证这一点，需要调用force()方法。
         *
         * force()方法有一个boolean类型的参数，指明是否同时将文件元数据（权限信息等）写到磁盘上。
         */
        inChannel.force(true);//将文件数据和元数据强制写到磁盘上：

        inChannel.close();

    }


    /**
     * Java NIO中的SocketChannel是一个连接到TCP网络套接字的通道。
     *
     * 可以通过以下2种方式创建SocketChannel：
     *
     *      打开一个SocketChannel并连接到互联网上的某台服务器。
     *
     *      一个新连接到达ServerSocketChannel时，会创建一个SocketChannel。
     *
     * @throws Exception
     */
    @Test
    public void SocketChannel() throws Exception{

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("http://www.baidu.com",80));


        /**
         * 首先，分配一个Buffer。从SocketChannel读取到的数据将会放到这个Buffer中。
         */
        ByteBuffer buffer = ByteBuffer.allocate(48);
        /**
         * 中。read()方法返回的int值表示读了多少字节进Buffer里。如果返回的是-1，表示已经读到了流的末尾（连接关闭了）。
         */
        int bytesRead = socketChannel.read(buffer);

        socketChannel.close();




    }

    /**
     * 写入 SocketChannel
     */
    @Test
    public void writeToSocketChannel() throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("http://www.baidu.com",80));

        String newData = "New string to write to file..." + System.currentTimeMillis();
        ByteBuffer buffer = ByteBuffer.allocate(48);
        buffer.clear();
        buffer.put(newData.getBytes());

        buffer.flip();

        /**
         * 注意SocketChannel.write()方法的调用是在一个while循环中的。
         * Write()方法无法保证能写多少字节到SocketChannel。
         * 所以，我们重复调用write()直到Buffer没有要写的字节为止。
         */
        while (buffer.hasRemaining()){
            socketChannel.write(buffer);
        }

        /**
         * 非阻塞模式
         * 可以设置 SocketChannel 为非阻塞模式（non-blocking mode）.
         * 设置之后，就可以在异步模式下调用connect(), read() 和write()了。
         *
         *connect()
         * 如果SocketChannel在非阻塞模式下，此时调用connect()，
         * 该方法可能在连接建立之前就返回了。
         * 为了确定连接是否建立，可以调用finishConnect()的方法。像这样：
         *
         */
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("http://www.baidu.com",80));

        while (! socketChannel.finishConnect()){
            //wait ,or do something else ...
        }
        /**
         * write()
         * 非阻塞模式下，write()方法在尚未写出任何内容时可能就返回了。
         * 所以需要在循环中调用write()。前面已经有例子了，这里就不赘述了。
         *
         *
         * read()
         * 非阻塞模式下,read()方法在尚未读取到任何数据时可能就返回了。
         * 所以需要关注它的int返回值，它会告诉你读取了多少字节。
         *
         * 非阻塞模式与选择器
         * 非阻塞模式与选择器搭配会工作的更好，
         * 通过将一或多个SocketChannel注册到Selector，
         * 可以询问选择器哪个通道已经准备好了读取，写入等。
         * Selector与SocketChannel的搭配使用会在后面详讲。
         */


    }

    /**
     * Java NIO中的 ServerSocketChannel 是一个可以监听新进来的TCP连接的通道，
     * 就像标准IO中的ServerSocket一样。
     * ServerSocketChannel类在 java.nio.channels包中。
     * @throws Exception
     */
    @Test
    public void ServerSocket() throws Exception{

        /**
         * 通过调用 ServerSocketChannel.open() 方法来打开ServerSocketChannel.如：
         */
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(9999));


        /**
         * 监听新进来的连接
         */
        while (true){
            /**
             * 通过 ServerSocketChannel.accept() 方法监听新进来的连接。
             * 当 accept()方法返回的时候，它返回一个包含新进来的连接的 SocketChannel。
             * 因此，accept()方法会一直阻塞到有新连接到达。
             */
            SocketChannel socketChannel  = serverSocketChannel.accept();


            /**
             * 为了后面代码生效
             */
            break;
            //do something with socketChannel...
        }

        serverSocketChannel.configureBlocking(false);
        while (true){

            /**
             * ServerSocketChannel可以设置成非阻塞模式。
             * 在非阻塞模式下，accept() 方法会立刻返回，
             * 如果还没有新进来的连接，返回的将是null。
             * 因此，需要检查返回的SocketChannel是否是null。如：
             */
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel!=null){
                //do something with socketChannel..

            }
        }




        //serverSocketChannel.close();
    }

    /**
     * Java NIO中的DatagramChannel是一个能收发UDP包的通道。
     * 因为UDP是无连接的网络协议，所以不能像其它通道那样读取和写入。它发送和接收的是数据包。
     * @throws Exception
     */
    @Test
    public  void  Datagram()  throws  Exception{
        DatagramChannel channel = DatagramChannel.open();
        channel.socket().bind(new InetSocketAddress(9999));

        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.clear();
        channel.receive(buffer);

        String newData = "New String to write to file..." + System.currentTimeMillis();

        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        buf.put(newData.getBytes());
        buf.flip();

        int bytesSent = channel.send(buf, new InetSocketAddress("jenkov.com", 80));


        channel.connect(new InetSocketAddress("jenkov.com", 80));
        int bytesRead = channel.read(buf);
        int bytesWritten = channel.write(buf);
    }


    /**
     * Java NIO 管道是2个线程之间的单向数据连接。
     * Pipe有一个source通道和一个sink通道。
     * 数据会被写到sink通道，从source通道读取。
     * @throws Exception
     */
    @Test
    public void Pipe() throws  Exception{

        /**
         * 创建管道
         */
        Pipe pipe = Pipe.open();

        /**
         * 向管道写数据
         * 要向管道写数据，需要访问sink通道
         */
        Pipe.SinkChannel sinkChannel = pipe.sink();
        String newData  = "New String to write to file ..."+System.currentTimeMillis();
        ByteBuffer byteBuffer = ByteBuffer.allocate(48);
        byteBuffer.clear();
        byteBuffer.put(newData.getBytes());
        byteBuffer.flip();
        while (true){
            sinkChannel.write(byteBuffer);

            /**
             * 为了后面代码能生效,暂时加上break
             */
            break;
        }

        /**
         * 从管道读取数据
         *
         * 从读取管道的数据，需要访问source通道
         */
        Pipe.SourceChannel sourceChannel = pipe.source();

        ByteBuffer readBuf = ByteBuffer.allocate(48);

        /**
         * read()方法返回的int值会告诉我们多少字节被读进了缓冲区。
         */
        int bytesRead = sourceChannel.read(readBuf);




    }

}

/**

 capacity

 作为一个内存块，Buffer有一个固定的大小值，也叫“capacity”.你只能往里写capacity个byte、long，char等类型。一旦Buffer满了，需要将其清空（通过读数据或者清除数据）才能继续写数据往里写数据。

 position

 当你写数据到Buffer中时，position表示当前的位置。初始的position值为0.当一个byte、long等数据写到Buffer后， position会向前移动到下一个可插入数据的Buffer单元。position最大可为capacity – 1。

 当读取数据时，也是从某个特定位置读。当将Buffer从写模式切换到读模式，position会被重置为0。当从Buffer的position处读取数据时，position向前移动到下一个可读的位置。

 limit

 在写模式下，Buffer的limit表示你最多能往Buffer里写多少数据。 写模式下，limit等于Buffer的capacity。

 当切换Buffer到读模式时， limit表示你最多能读到多少数据。因此，当切换Buffer到读模式时，limit会被设置成写模式下的position值。换句话说，你能读到之前写入的所有数据（limit被设置成已写数据的数量，这个值在写模式下就是position）




 clear():使缓冲区为一系列新的通道读取或相对放置 操作做好准备：它将限制设置为容量大小，将位置设置为 0。

 清除此缓冲区。将位置设置为 0，将限制设置为容量，并丢弃标记。

 在使用一系列通道读取或放置 操作填充此缓冲区之前调用此方法

 flip():使缓冲区为一系列新的通道写入或相对获取 操作做好准备：它将限制设置为当前位置，然后将位置设置为 0。

 反转此缓冲区。首先将限制设置为当前位置，然后将位置设置为 0。如果已定义了标记，则丢弃该标记。

 在一系列通道读取或放置 操作之后，调用此方法为一系列通道写入或相对获取 操作做好准备。例如：

 buf.put(magic);    // Prepend header
 in.read(buf);      // Read data into rest of buffer
 buf.flip();        // Flip buffer
 out.write(buf);    // Write header + data to channel

 rewind():使缓冲区为重新读取已包含的数据做好准备：它使限制保持不变，将位置设置为 0。





 */
