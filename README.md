# File-Retrieval-Engine-POSIX-Sockets

### Requirements

If you are implementing your solution in C++ you will need to have GCC 12.x and CMake 3.22.x installed on your system.
On Ubuntu 22.04 LTS you can install GCC and set it as default compiler using the following commands:


sudo apt install g++-12 gcc-12 cmake
sudo update-alternatives --remove-all gcc
sudo update-alternatives --install /usr/bin/gcc gcc /usr/bin/gcc-11 110
sudo update-alternatives --install /usr/bin/gcc gcc /usr/bin/gcc-12 120
sudo update-alternatives --remove-all g++
sudo update-alternatives --install /usr/bin/g++ g++ /usr/bin/g++-11 110
sudo update-alternatives --install /usr/bin/g++ g++ /usr/bin/g++-12 120


If you are implementing your solution in Java you will need to have Java 17.x and Maven 3.6.x installed on your systems.
On Ubuntu 22.04 LTS you can install Java and Maven using the following commands:


sudo apt install openjdk-17-jdk maven



### Setup

There are 5 datasets (Dataset1, Dataset2, Dataset3, Dataset4, Dataset5) that you need to use to evaluate the indexing performance of your solution.
Before you can evaluate your solution you need to download the datasets. You can download the datasets from the following link:

https://depauledu-my.sharepoint.com/:f:/g/personal/aorhean_depaul_edu/EgmxmSiWjpVMi8r6QHovyYIB-XWjqOmQwuINCd9N_Ppnug?e=TLBF4V

After you finished downloading the datasets copy them to the dataset directory (create the directory if it does not exist).
Here is an example on how you can copy Dataset1 to the remote machine and how to unzip the dataset:


remote-computer$ mkdir datasets
local-computer$ scp Dataset1.zip cc@<remote-ip>:<path-to-repo>/datasets/.
remote-computer$ cd <path-to-repo>/datasets
remote-computer$ unzip Dataset1.zip


### Java solution
#### How to build/compile

To build the Java solution use the following commands:

cd app-java

mvn compile

mvn package


#### How to run application

To run the Java server (after you build the project) use the following command:
java -cp target/app-java-1.0-SNAPSHOT.jar csc435.app.FileRetrievalServer <port>



To run the Java client (after you build the project) use the following command:
java -cp target/app-java-1.0-SNAPSHOT.jar csc435.app.FileRetrievalClient


### Example (2 clients and 1 server)

**Step 1:start the server:**

Server

java -cp target/app-java-1.0-SNAPSHOT.jar csc435.app.FileRetrievalServer
>

**Step 2: start the clients and connect them to the server:**
Client 1

java -cp target/app-java-1.0-SNAPSHOT.jar csc435.app.FileRetrievalClient
> connect 127.0.0.1 12345
Connection successful!


Client 2

java -cp target/app-java-1.0-SNAPSHOT.jar csc435.app.FileRetrievalClient
> connect 127.0.0.1 12345
Connection successful!


**Step 3: list the connected clients on the server:**

Server
> list
client1: 127.0.0.1 5746
client2: 127.0.0.1 9677


**Step 4: index files from the clients:**
Client 1

> index /home/kinjal/datasets/Dataset1/folder1
Indexing time: 1.857243844 seconds
Indexing completed!
> index /home/kinjal/datasets/Dataset1/folder3
Indexing time: 1.575727692 seconds
Indexing completed!
> index /home/kinjal/datasets/Dataset1/folder5
Indexing time: 0.723099484 seconds
Indexing completed!
> index /home/kinjal/datasets/Dataset1/folder7
Indexing time: 0.869891497 seconds
Indexing completed!
> index /home/kinjal/datasets/Dataset1/folder9
Indexing time: 0.805021187 seconds
Indexing completed!
> index /home/kinjal/datasets/Dataset1/folder11
Indexing time: 1.015170042 seconds
Indexing completed!
> index /home/kinjal/datasets/Dataset1/folder13
Indexing time: 1.001032023 seconds
Indexing completed!
> index /home/kinjal/datasets/Dataset1/folder15
Indexing time: 1.549955875 seconds
Indexing completed!



Client 2

> index /home/kinjal/datasets/Dataset1/folder2
Indexing time: 2.259412692 seconds
Indexing completed!
> index /home/kinjal/datasets/Dataset1/folder4
Indexing time: 1.119753113 seconds
Indexing completed!
> index /home/kinjal/datasets/Dataset1/folder6
Indexing time: 1.316628694 seconds
Indexing completed!
> index /home/kinjal/datasets/Dataset1/folder8
Indexing time: 1.396327066 seconds
Indexing completed!
> index /home/kinjal/datasets/Dataset1/folder10
Indexing time: 1.626541874 seconds
Indexing completed!
> index /home/kinjal/datasets/Dataset1/folder12
Indexing time: 0.814861155 seconds
Indexing completed!
> index /home/kinjal/datasets/Dataset1/folder14
Indexing time: 0.958238251 seconds
Indexing completed!
> index /home/kinjal/datasets/Dataset1/folder16
Indexing time: 1.067063342 seconds
Indexing completed!



**Step 5: search files from the clients:**

Client 1

> search worms
Search time: 0.027995477 seconds
Search results: /home/kinjal/datasets/Dataset1/folder11/document286.txt 14
/home/kinjal/datasets/Dataset1/folder10/document30.txt 8
/home/kinjal/datasets/Dataset1/folder3/document10.txt 8
/home/kinjal/datasets/Dataset1/folder6/document200.txt 7
/home/kinjal/datasets/Dataset1/folder10/document124.txt 6
/home/kinjal/datasets/Dataset1/folder11/document270.txt 6
/home/kinjal/datasets/Dataset1/folder10/document430.txt 4
/home/kinjal/datasets/Dataset1/folder13/document450.txt 3
/home/kinjal/datasets/Dataset1/folder1/document291.txt 3
/home/kinjal/datasets/Dataset1/folder15/document111.txt 3


Client 2

> search distortion AND adaptation
Search time: 0.016841137 seconds
Search results: /home/kinjal/datasets/Dataset1/folder6/document200.txt 49
/home/kinjal/datasets/Dataset1/folder5/document474.txt 15
/home/kinjal/datasets/Dataset1/folder8/document22.txt 6
/home/kinjal/datasets/Dataset1/folder8/document477.txt 5
/home/kinjal/datasets/Dataset1/folder13/document38.txt 4
/home/kinjal/datasets/Dataset1/folder6/document408.txt 3
/home/kinjal/datasets/Dataset1/folder8/document252.txt 3
/home/kinjal/datasets/Dataset1/folder2/document404.txt 3
/home/kinjal/datasets/Dataset1/folder6/document475.txt 3
/home/kinjal/datasets/Dataset1/folder9/document77.txt 2



**Step 6: close and disconnect the clients:**

Client 1
> quit

Client 2
> quit

**Step 7: close the server:**

Server
> quit
