Objective: remove all mails in @gmail.com account

&nbsp;

I used this program to delete my 6,000+ emails in my gmail account, freeing up several GBs of space in my Google Drive.

&nbsp;

Method: 
* (1) move all mails in the INBOX to TRASH, 
 * (2) flag all mails in the TRASH as deleted, then
 * (3) expunge the TRASH folder 

&nbsp;

For this app to be able to access gmail,

 * (1) In EmailService.java, specify your username/password in EmailService.java
 * (2) In EmailService.java, specify in exclude_signature_sender, the email addresses and names, whose emails you do not want to be deleted
 * (3) allow less secure apps at https://myaccount.google.com/lesssecureapps
 * (4) replace /lib/mail.jar with the latest version from the javamail library and add it to the build path

&nbsp;

After running this app, disallow the access for less secure apps, as in step (3), for security.

&nbsp;

References:
* https://stackoverflow.com/questions/39438810/java-mail-api-delete-mails-from-gmail
 * https://javaee.github.io/javamail/FAQ#gmaildelete

&nbsp;

