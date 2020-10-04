# Spring Security Türkçe

Merhaba! Bu proje jwt token ile spring security uygulamasının nasıl hazırlanacağı hususunda türkçe kaynak oluşturmak amacı ile hazırlanmıştır.
Kod içerisinde Java'da hiç bir kodun sihirli olmadığını anlatmak için bol bol yorum satırı girmeye ve bu yorum satırları içerisinde 
ilgili konulara referans vermeye çalıştım. Anlaşımayan bir nokta olması halinde issue açabilir 
ve bu issue üzerinden tartışarak birbirimize bir şeyler katabiliriz.

## Kurulum

**Kurulum için gerekli araçlar:**
* [OpenJDK 8](https://openjdk.java.net/install/) veya üzeri bir sürüm. 
* Bunun dışında projeyi yerele çekebilmek için [Git](https://git-scm.com/downloads) kullanılabilir. Eğer git kurmak istemezseniz 
[Github](https://github.com/burakkggul/issue-management/archive/master.zip) arayüzü üzerinden proje dosyalarını indirebilirsiniz.

**Kurulum Adımları:**
* Github repositorysi yerele çekilir.
* Projenin root dizininde terminal aracılığı ile `./mvnw spring-boot:run` komutu çalıştırılır.

## Kullanım

**Kullanım için tavsiye edilen araçlar:**
* [JMeter](https://jmeter.apache.org/download_jmeter.cgi)

Jmeter için hazırlanmış testler uygulama içerisinde `func_test.jmx` adı ile bulunmaktadır.
Bu dosyayı yukarıda bulunan link aracılığıyla kurduğunuz jmeter ile açarak testleri yapabilirsiniz.
Bunun dışında farklı bir metod ile kullanmak isterseniz **aşağıda bulunan adımları** uygulamanız gerekmektedir.
* **/auth/signup** endpointine request body içerisinde aşağıdaki gibi bir JSON gönderilmelidir.
```
{
"username":"ornek_username",
"password":"ornek_password"
}
```
* **/auth/login** endpointine request body içerisinde **/auth/signup** da göndermiş olduğunuz username ve password bilgilerini
aşağıdaki gibi bir JSON ile göndermeniz gerekmektedir.
```
{
"username":"ornek_username",
"password":"ornek_password"
}
```

* Yukarıda bulunan adımları sorunsuz tamamlamanız halinde `/healthCheck` endpointine atacağınız bir 
GET isteğine 200 http durum kodu ile OK cevabını almalısınız.
 

## Katılım
Şu kısım daha güzel yazılabilirdi, şuraya şu şekilde bir yorum satırı eklenebilir gibi fikirleriniz için Pull Request gönderebilirsiniz.

## Lisans
[GPL v3](http://www.gnu.org/licenses/gpl-3.0.html) - [Türkçe Lisans Metni](http://ozgurlisanslar.org.tr/gpl/gpl-v3/)
