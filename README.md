# LAN Chat App

A simple, lightweight Local Area Network (LAN) chat application built in Java. It allows multiple users on the same local network to join a shared chat room and exchange messages in real-time.

## 🛠 Tech Stack

- **Language:** Java
- **Networking:** UDP Multicast Sockets (`java.net.MulticastSocket`)
- **Interface:** Command Line Interface (CLI)

The application uses UDP Multicast (Group Address: `230.0.0.1`, Port: `4446`) to broadcast and receive messages across the local network without requiring a dedicated central server.

## 🚀 How to Run on Your Device

### Prerequisites
- **Java Development Kit (JDK)** 8 or higher installed on your system.

### 1. Compile the Source Code

Open your terminal, navigate to the root directory of this project, and compile the Java files into a `bin` folder:

```bash
mkdir -p bin
find src -name "*.java" > sources.txt
javac -d bin @sources.txt
rm sources.txt
```
*(This compiling method works across all Unix/macOS environments avoiding command line length limits).*

### 2. Start the Application

Run the compiled application by specifying your desired username as a command-line argument:

```bash
java -cp bin chat.app.ChatApplication <YourUsername>
```

**Example:**
```bash
java -cp bin chat.app.ChatApplication Alice
```

### 3. Chat with Others!

To test it on your own machine, open a **second terminal window** and run the app with a different username:

```bash
java -cp bin chat.app.ChatApplication Bob
```

Start typing your messages in the terminal and press `Enter` to send them! Type `/quit` or use `Ctrl+C` to exit the chat.
