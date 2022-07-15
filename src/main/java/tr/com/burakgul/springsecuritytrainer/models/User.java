package tr.com.burakgul.springsecuritytrainer.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Kullanıcı bilgilerini tutmak için kullanacağımız veri erişim objesi. (Data Acces Object DAO)
 * @author Burak GUL 16.09.2020
 */
@Data //@ToString, @EqualsAndHashCode, @Getter / @Setter ve @RequiredArgsConstructor 'ı tek bir anotasyon ile kullanmak için
//bir kısayoldur.
@NoArgsConstructor //Parametre almayan bir constructor oluşturmak için kullanılır.
@AllArgsConstructor //Tüm parametreleri içeren bir constructor oluşturmak için kullanılır.
@Entity //Bu modelin bir database entitysi olduğunu ifade eder. Class'ı entity bean olarak işaretler.
@Table(name = "users") //Table olduğunu belirtip bu table'a bir isim veriyoruz. Bu anotasyon'un verilmesi zorunlu değildir.
// Entity'e ekstra özellikler kazandırılmak istenirse (biz şimdilik sadece isim vermek için kullandık) bu anotasyon kullanılabilir.
//Eğer kullanılmazsa isim olarak class'ın ismini alır.
public class User {

    @Id //Her entity bir primary key içermelidir. Bu primary key'in hangi field olduğunu belirtmek için bu anotasyonu kullanıyoruz.
    @GeneratedValue(strategy = GenerationType.AUTO)   //Bu field'ın otomatik generate edilen bir field olduğunu belirtiyoruz.
    // 1'den başlayıp birer birer artacak.
    @Column(name="id") //Bir tablo kolonu olduğunu belirtip bu kolona bir isim veriyoruz. Belirtilmesi zorunlu değildir.
    //Belirtilmediği takdirde field'ın adını alır.
    private Long id;

    @Column(name = "username", nullable = false, unique = true) //Bir tablo kolonu olduğunu belirtip bu kolona bir isim veriyoruz.
    //Bu kolonda bulunan değerlerin null olamayacağını ve birbirlerinden farklı olması gerektiğini belirtiyoruz.
    //Bu colonlara name vermediğimiz takdirde field isimlerini alacaklardır.
    private String username;

    @Column(name = "password" ,nullable = false)
    private String password;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

}
