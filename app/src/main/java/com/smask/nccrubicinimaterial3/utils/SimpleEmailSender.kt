import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.Socket
import java.net.UnknownHostException
import java.util.*

/**
 * The class of the kotlinSimpleEmailSender,
 * no parameter required when calling an instance of it.
 * You can use setServer() method to set a specific smtp server
 * setHost() method set the smtp server host port
 * setUsername() method set the username to login smtp server
 * setPassword() method set the password to login smtp server
 * setFrom() method set the sender email
 * setTo() method set the receiver email
 * setSubject() method set the email subject
 * setContent() method set the email content
 *
 */
class SimpleEmailSender {
    private var socket: Socket? = null
    private var streamIn: InputStream? = null
    private var streamOut: OutputStream? = null
    private var isConnected: Boolean = false
    private var smtpServer: String? = null

    private var smtpHost: Int? = null
    private var encrypt: String? = null
    private var username: String? = null
    private var password: String? = null
    private var emailBody: Email = Email()

    private fun sendMessage(outMessage: String?): SimpleEmailSender {
        var sendMsg: String = outMessage!!
        sendMsg = String.format("%s\r\n", sendMsg)
        try {
            streamOut!!.write(sendMsg.toByteArray())
            streamOut!!.flush()
            println(">>Message has sent:$outMessage")
        } catch (IOExp: IOException) {
            throw IOException(">>Message sent failed for IO exception.\r\n")
        }
        return this
    }

    private fun receiveMessage(): String? {
        val message: String
        val inStream = InputStreamReader(streamIn!!)
        try {
            val rev = CharArray(1024)
            val a = inStream.read(rev)
            if (a <= -1) {
                return null
            }
            message = String(rev, 0, a)
            println("<<Received message: $message")
        } catch (IOExp: IOException) {
            throw Exception(">>Message receiving failed for IO exception.\r\n")
        }
        return message
    }

    private fun connect() {
        try {
            socket = Socket(smtpServer!!, smtpHost!!)
            socket!!.soTimeout = 60000
            streamIn = socket!!.getInputStream()
            streamOut = socket!!.getOutputStream()

            if (socket == null || streamIn == null || streamOut == null) {
                println(">>Failed to build socket connection with provided server.\r\n")
            } else {
                isConnected = true
                println(">>Socket connection with smtp server succeed at server: $smtpServer, server host: $smtpHost.\r\n")

            }
        } catch (ioException: IOException) {
            throw Exception(">>Connection could not be established for io exception.\r\n")
        } catch (unKnowHost: UnknownHostException) {
            throw Exception(">>Connection could not be established for unknown host exception.\r\n")
        }
    }

    private fun afterConnect(): SimpleEmailSender {
        println("<<${this.receiveMessage()}")
        return this
    }

    private fun encodeFn(str: String): String {
        return Base64.getEncoder().encodeToString(str.toByteArray())
    }

    private fun getRetCode(retMsg: String): String? {
        if (retMsg.length < 3) {
            return null
        }
        return retMsg.substring(0..2)
    }

    private fun checkRetCode(retCode: String) {
        val statusCode = getRetCode(retCode)
        if (retCode[0] == '5') {
            throw Exception(">>Server returned error:\n$retCode")
        } else if (retCode[0] == '4') {
            throw Exception(">>Server returned error:\n$retCode")
        }
    }

    private fun sayHello(): SimpleEmailSender {
        val serverMsg = this.sendMessage("helo a").receiveMessage()
        if (getRetCode(serverMsg!!) != "250") {
            throw Exception("<<Expected code 250, but got $serverMsg\r\n")
        }
        return this
    }

    private fun sayAuth(): SimpleEmailSender {
        val serverMsg = this.sendMessage("AUTH LOGIN").receiveMessage()
        if (getRetCode(serverMsg!!) != "334") {
            throw Exception("<<Expected code 334, but got $serverMsg\r\n")
        }
        return this
    }

    private fun login(): SimpleEmailSender {
        val authKey = this.sendMessage(this.encodeFn(username!!)).receiveMessage()
        if (getRetCode(authKey!!) != "334") {
            throw Exception("<<Expected code 334, but got $authKey\r\n")
        }
        val authPsd = this.sendMessage(this.encodeFn(password!!)).receiveMessage()
        if (getRetCode(authPsd!!) != "235") {
            throw Exception("<<Expected code 235, but got $authPsd\r\n")
        }
        return this
    }

    private fun sayFrom(): SimpleEmailSender {
        val serverMsg = this.sendMessage("mail from:<${emailBody.from}>").receiveMessage()
        if (getRetCode(serverMsg!!) != "250") {
            throw Exception("<<Expected code 250, but got $serverMsg\r\n")
        }
        return this
    }

    private fun sayTo(): SimpleEmailSender {
        val serverMsg = this.sendMessage("rcpt to:<${emailBody.to}>").receiveMessage()
        if (getRetCode(serverMsg!!) != "250") {
            throw Exception("<<Expected code 250, but got $serverMsg\r\n")
        }
        return this
    }

    private fun sayData(): SimpleEmailSender {
        val serverMsg = this.sendMessage("data\r\n").receiveMessage()
        if (getRetCode(serverMsg!!) != "354") {
            throw Exception("<<Expected code 354, but got $serverMsg\r\n")
        }
        return this
    }

    private fun sayContent(): SimpleEmailSender {
        val mailStr = emailBody.buildEmailBody()
        val serverMsg = this.sendMessage(mailStr).receiveMessage()
        if (getRetCode(serverMsg!!) != "250") {
            throw Exception("<<Expected code 250, but got $serverMsg\r\n")
        }
        return this
    }

    private fun sayQuit(): SimpleEmailSender {
        println("<<${this.sendMessage("quit").receiveMessage()}")
        return this
    }

    private fun close() {
        var afterBye = this.receiveMessage()
        while (afterBye != null && isConnected) {
            afterBye = this.receiveMessage()
        }
        socket!!.close()
        isConnected = false
    }

    fun send() {
        this.connect()
        if (!isConnected || socket == null || streamIn == null || streamOut == null) {
            throw Exception(">>Socket connection failed")
        }
        this.afterConnect()
            .sayHello()
            .sayAuth()
            .login()
            .sayFrom()
            .sayTo()
            .sayData()
            .sayContent()
            .sayQuit()
            .close()
    }

    //The email data class,
    //which contains email sender, receiver, subject and content fields.
    data class Email(
        var from: String? = "null",
        var to: String? = "",
        var subject: String = "",
        var content: String = ""
    ) {
        fun buildEmailBody(): String {
            if (from == null) {
                throw Exception(">>no email sender, Email cannot send!\r\n")
            }
            if (to == null) {
                throw Exception(">>no email receiver, Email cannot send!\r\n")
            }
            return "from:${this.from}\r\nto:${this.to}\r\nSubject:${this.subject}\r\n\r\n${this.content}\r\n.\r\n"
        }
    }

    //set the sender email
    fun setFrom(from: String): SimpleEmailSender {
        emailBody.from = from
        return this
    }

    //set the receiver email
    fun setTo(to: String): SimpleEmailSender {
        emailBody.to = to
        return this
    }

    //set email subject
    fun setSubject(subject: String): SimpleEmailSender {
        emailBody.subject = subject
        return this
    }

    //set email content
    fun setContent(content: String): SimpleEmailSender {
        emailBody.content = content
        return this
    }

    //method to set smtp server encrypt type, not used yet
    fun setEncrypt(encryptProto: String): SimpleEmailSender {
        encrypt = encryptProto.lowercase()
        return this
    }

    //method to set smtp server
    fun setServer(server: String): SimpleEmailSender {
        smtpServer = server
        return this
    }

    //method to set smtp server host
    fun setHost(host: Int): SimpleEmailSender {
        smtpHost = host
        return this
    }

    //method to set the username on smtp server
    fun setUsername(name: String): SimpleEmailSender {
        username = name
        return this
    }

    //method to set the password on smtp server
    fun setPassword(psd: String): SimpleEmailSender {
        password = psd
        return this
    }

}