Objective: remove all mails in @gmail.com account

&nbsp;

I used this program to **delete my 6,000+ old gmails**, **freeing up several GBs of space in my Google Drive**.

&nbsp;

Method Summary: 
* (1) move all mails to the TRASH folder, 
 * (2) flag all mails in the TRASH as deleted

&nbsp;

To run this program, 

 * (1) Import this project into **Eclipse IDE**
 * (2) In EmailService.java, specify your **username/password**
 * (3) In EmailService.java, specify in **exclude_signature_sender**, the email addresses and names, whose emails you do not want to be deleted
 * (4) allow gmail-access from less secure apps at https://myaccount.google.com/lesssecureapps
 * (5) **(optional)** replace /lib/mail.jar with the latest version from the javamail library and add it to the build path

&nbsp;

After running this program, disallow the access for less secure apps, as in step (4), for security.

&nbsp;

References:
* https://stackoverflow.com/questions/39438810/java-mail-api-delete-mails-from-gmail
 * https://javaee.github.io/javamail/FAQ#gmaildelete

&nbsp;

