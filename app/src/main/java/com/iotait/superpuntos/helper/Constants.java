package com.iotait.superpuntos.helper;

import android.util.Log;

import com.iotait.superpuntos.fcm.ApiClient;
import com.iotait.superpuntos.fcm.ApiInterface;
import com.iotait.superpuntos.fcm.DataModel;
import com.iotait.superpuntos.fcm.NotificationModel;
import com.iotait.superpuntos.fcm.RootModel;
import com.iotait.superpuntos.adapter.SurveyAdapter;
import com.iotait.superpuntos.models.ClaimedReward;
import com.iotait.superpuntos.models.Contact;
import com.iotait.superpuntos.models.Question;
import com.iotait.superpuntos.models.Survey;
import com.iotait.superpuntos.models.User;
import com.iotait.superpuntos.models.UserActivity;
import com.iotait.superpuntos.models.UserGroup;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Callback;

public class Constants {
    public static String USERVERSION;
    public static final String SERVER_KEY = "AAAADOEj2eU:APA91bH63G0rIFfGOHZkMUIGUCN6wKNyTQEa5SZSVqIrpSlcwXj54w_Lxy7r2CP031uNJZrbwL3F62ixJPmuXeHkEW7cbU1ANg2L0Dx79Ig68tFKipnZ78emnMrQojfCDZmNTX8bYf0C";
    public static User USER = new User();
    public static String DELETE_KEY;
    public static HashMap <String, UserGroup> GROUPSMAP = new HashMap();
    //for test mode set the value to "testenvironment/" and for production mode set the value to "".
    public static String TEST="testenvironment/";
    public static String APPNAME="com.iotait.superpuntos"; //"com.miui.player";//"com.facebook.katana";
    public static Survey SURVEY;
    public static final String TERMS="1. Términos\n" +
            "Al acceder nuestro app, SuperPuntos, usted acepta regirse por estos términos de servicio, todas las leyes y regulaciones aplicables y acepta que es responsable del cumplimiento de las leyes locales aplicables. Si no está de acuerdo con alguno de estos términos, tiene prohibido utilizar o acceder SuperPuntos. Los materiales contenidos en SuperPuntos están protegidos por las leyes de derechos de autor y de marcas registradas.\n" +
            "\n" +
            "2. Licencia de uso\n" +
            "Se otorga el permiso para descargar temporalmente una copia de SuperPuntos para visualización transitoria personal, no de uso comercial. Esta es la concesión de una licencia, no una transferencia de título, y bajo esta licencia usted no puede:\n" +
            "modificar o copiar los materiales;\n" +
            "usar los materiales para cualquier propósito comercial, o para cualquier exhibición pública (comercial o no comercial);\n" +
            "intentar descompilar o aplicar ingeniería inversa a cualquier software contenido en SuperPuntos;\n" +
            "eliminar cualquier copyright u otra notación de propiedad de los materiales; o\n" +
            "transferir los materiales a otra persona o mostrar los materiales en cualquier otro servidor.\n" +
            "Esta licencia terminará automáticamente si incumple cualquiera de estas restricciones y SuperPuntos podrá rescindirla en cualquier momento. Al finalizar la visualización de estos materiales o al finalizar esta licencia, debe destruir cualquier material descargado en su poder, ya sea en formato electrónico o impreso.\n" +
            "\n" +
            "3. Aviso legal\n" +
            "Los materiales en SuperPuntos se proporcionan \"tal cual\". SuperPuntos no ofrece ninguna garantía, expresa o implícita, y por este medio niega todas las demás garantías incluidas, sin limitación, las garantías implícitas o las condiciones de comercialización, idoneidad para un fin particular o no infracción de la propiedad intelectual u otra violación de los derechos.\n" +
            "Además, SuperPuntos no garantiza ni hace ninguna declaración con respecto a la precisión, los resultados probables o la fiabilidad del uso de los materiales en su app o de otro modo relacionado con dichos materiales o en cualquier sitio vinculado a este sitio.\n" +
            "\n" +
            "4. Limitaciones\n" +
            "En ningún caso, SuperPuntos o sus proveedores serán responsables de los daños (incluidos, entre otros, los daños por pérdida de datos o ganancias, o debido a la interrupción del negocio) que surjan del uso o la imposibilidad de utilizar los materiales en SuperPuntos, incluso si la SuperPuntos o un representante autorizado de SuperPuntos han sido notificados verbalmente o por escrito sobre la posibilidad de tal daño. Debido a que algunas jurisdicciones no permiten limitaciones sobre garantías implícitas o limitaciones de responsabilidad por daños indirectos o incidentales, estas limitaciones pueden no ser de aplicación en su caso.\n" +
            "5. Precisión de los materiales\n" +
            "Los materiales que aparecen en SuperPuntos podrían incluir errores técnicos, tipográficos o fotográficos. SuperPuntos no garantiza que ninguno de los materiales en SuperPuntos sea preciso, completo o actual. SuperPuntos puede hacer cambios a los materiales contenidos en SuperPuntos en cualquier momento sin previo aviso. Sin embargo, SuperPuntos no se compromete a actualizar dichos materiales.\n" +
            "\n" +
            "6. Enlaces\n" +
            "SuperPuntos no ha revisado todos los sitios vinculados a su app y no es responsable de los contenidos de ningún app vinculado. La inclusión de cualquier enlace no implica el respaldo del app por parte de SuperPuntos. El uso de cualquier sitio web vinculado es por cuenta y riesgo del usuario.\n" +
            "\n" +
            "7. Modificaciones\n" +
            "SuperPuntos puede revisar estos términos de servicio para su app en cualquier momento sin previo aviso. Al utilizar SuperPuntos, usted acepta regirse por la versión actual de estos términos de servicio.\n" +
            "\n" +
            "8. Legislación\n" +
            "Estos términos y condiciones se rigen y se interpretan de acuerdo con las leyes de Florida, USA y usted se somete irrevocablemente a la jurisdicción exclusiva de los tribunales de ese estado o ubicación.\n";

    public static final String PRIVACY = "Su privacidad es importante para nosotros. La política de SuperPuntos es respetar su privacidad en relación a cualquier información suya que podamos recopilar en app, SuperPuntos, y sus servicios asociados.\n" +
            "\n" +
            "1. Información que recopilamos\n" +
            "Dato de registro\n" +
            "Cuando acceso a nuestros servidores vía SuperPuntos, nosotros podrá automáticamente registrar los datos estándar facilitados por tu dispositivo. Puede incluir la dirección de Protocolo de Internet (IP) de su ordenador, tu el tipo de dispositivo y versión, su actividad en la app, tiempo y fecha y otros detalles.\n" +
            "Además, cuando encuentra ciertos errores mientras utiliza la aplicación, recopilamos automáticamente datos sobre el error y las circunstancias que lo rodean. Estos datos pueden incluir detalles técnicos sobre su dispositivo, lo que intentaba hacer cuando ocurrió el error y otra información técnica que puede haber contribuido al problema.\n" +
            "Datos del dispositivo\n" +
            "Nuestra aplicación también puede acceder y recopilar datos a través de las herramientas integradas de su dispositivo, tales como:\n" +
            "Su identidad\n" +
            "Datos de ubicación\n" +
            "Cámara\n" +
            "Contactos\n" +
            "Teléfono/SMS\n" +
            "Datos móviles\n" +
            "Historial del dispositivo/app\n" +
            "Lo que recopilamos puede depender de la configuración individual de su dispositivo y de los permisos que otorgue al instalar y utilizar la aplicación.\n" +
            "Información personal\n" +
            "Podemos solicitar información personal, como:\n" +
            "Nombre\n" +
            "Correo electrónico\n" +
            "Perfiles de redes sociales\n" +
            "Fecha de nacimiento\n" +
            "Número de teléfono/móvil\n" +
            "Domicilio/dirección postal\n" +
            "Datos de Negocio\n" +
            "Los datos de negocio se refieren a los datos que se acumulan durante el curso normal de operación en nuestra plataforma. Esto puede incluir registros de transacciones, archivos almacenados, perfiles de usuario, datos analíticos y otros datos medibles, así como otros tipos de información, creada o generada, a medida que los usuarios interactúan con nuestros servicios.\n" +
            "\n" +
            "2. Bases legales de procesamiento\n" +
            "Procesaremos su información personal de manera legal, justa y transparente. Recopilamos y procesamos información sobre usted solo donde tenemos bases legales para hacerlo.\n" +
            "Estas bases legales dependen de los servicios que usa y de cómo los usa, lo que significa que recopilamos y usamos su información solo cuando:\n" +
            "es necesario para la ejecución de un contrato en el que usted es parte o para comenzar a avanzar en su solicitud antes de comenzar con dicho contrato (por ejemplo, cuando le ofrecemos un servicio que nos solicita);\n" +
            "satisface un interés legítimo (que no está anulado por sus intereses de protección de datos), como investigación y desarrollo, para comercializar y promover nuestros servicios, y para proteger nuestros derechos e intereses legales;\n" +
            "nos da su consentimiento para hacerlo con un propósito específico (por ejemplo, puede darnos su consentimiento para que le enviemos nuestro boletín informativo); o\n" +
            "necesitamos procesar sus datos para cumplir con una obligación legal.\n" +
            "Cuando usted acepta nuestro uso de su información para un propósito específico, tiene derecho a cambiar de opinión en cualquier momento (pero esto no afectará a ningún procesamiento que ya haya tenido lugar).\n" +
            "No guardamos información personal por más tiempo del necesario. Mientras conservamos esta información, la protegeremos con medios comercialmente aceptables para evitar pérdidas y robos, así como el acceso, divulgación, copia, uso o modificación no autorizados. Dicho esto, recordamos que ningún método de transmisión o almacenamiento electrónico es 100% seguro y no pueden garantizar la seguridad absoluta de los datos. Si es necesario, podemos conservar su información personal para cumplir con una obligación legal o para proteger sus intereses vitales o los intereses vitales de otra persona física.\n" +
            "\n" +
            "3. Recopilación y uso de la información\n" +
            "Podemos recopilar, retener, usar y divulgar información para los siguientes fines y la información personal no se procesará de manera incompatible con estos fines:\n" +
            "para la evaluación técnica, incluyendo para operar y mejorar nuestra aplicación, aplicaciones asociadas y plataformas de redes sociales asociadas;para proporcionarle las características principales de nuestra app y plataforma;para permitirle acceder y utilizar nuestra app, plataformas asociadas y canales de medios sociales asociados;para contactar y comunicar con usted;para registro interno y fines administrativos;para análisis, investigación de mercado y desarrollo de negocios, incluso para operar y mejorar nuestra aplicación, aplicaciones asociadas y plataformas de redes sociales asociadas;para promover competiciones y/u ofrecer beneficios adicionales para usted;para publicidad y marketing, incluido el envío de información promocional sobre nuestros productos y servicios e información sobre terceros que consideramos pueden ser de su interés;para cumplir con nuestras obligaciones legales y resolver cualquier disputa que podamos tener; ypara considerar su solicitud de empleo.\n" +
            "\n" +
            "4. Divulgación de información personal a terceras partes\n" +
            "Podemos divulgar información personal a:\n" +
            "proveedores de servicios de tercera parte con el fin de permitirles brindar sus servicios, incluidos (sin limitación) proveedores de servicios de informática, almacenamiento de datos, servidores de alojamientos web y servidores, redes publicitarias, analíticas, registradores de errores, cobradores de deudas, proveedores de servicios de mantenimiento o resolución de problemas, proveedores de marketing o publicidad, asesores profesionales y operadores de sistemas de pago;nuestros empleados, contratistas y/o entidades relacionadas;patrocinadores o promotores de cualquier competición organizada por nuestra empresa;juzgados, tribunales, autoridades reguladoras y funcionarios encargados de hacer cumplir la ley, en relación con cualquier procedimiento legal actual o potencial para establecer, ejercer o defender nuestros derechos legales;terceros, incluidos agentes o subcontratistas, que nos ayudan a proporcionarle información, productos, servicios o marketing directo; yterceras partes que recopilan y procesan datos.\n" +
            "\n" +
            "5. Transferencias Internacionales de información personal\n" +
            "La información personal que recopilamos se almacena y procesa en Estados Unidos, o donde nosotros o nuestros socios, afiliados y proveedores externos tengamos instalaciones. Al proporcionarnos su información personal, usted acepta la divulgación a estos países terceros.\n" +
            "Nos aseguraremos de que cualquier transferencia de información personal desde países del Espacio Económico Europeo (EEE) a países fuera del EEE se proteja mediante las medidas apropiadas, por ejemplo, mediante el uso de cláusulas de protección de datos estándar aprobadas por la Comisión Europea o el uso normas vinculantes corporativas u otros medios legalmente aceptados.\n" +
            "Cuando transferimos información personal de un país no perteneciente al EEE a otro país, usted reconoce que terceros en otras jurisdicciones pueden no estar sujetos a leyes de protección de datos similares a las de nuestra jurisdicción. Existen riesgos si cualquiera de dichas terceras partes se involucra en algún acto o práctica que contravenga las leyes de privacidad de los datos en nuestra jurisdicción y esto podría significar que no podrá solicitar una rectificación conforme a las leyes de privacidad de nuestra jurisdicción.\n" +
            "\n" +
            "6. Sus derechos y el control de su información personal\n" +
            "Elección y consentimiento: Al proporcionarnos información personal, usted nos autoriza a recopilar, retener, usar y divulgar su información personal de acuerdo con esta política de privacidad. Si tiene menos de 16 años de edad, debe tener y garantizar, en la medida en que lo permita la ley, que tiene el permiso de su padre o tutor legal para acceder y utilizar la app y que ellos (sus padres o tutor) han dado su consentimiento para que usted nos proporcione su información personal. No es necesario que nos proporcione información personal; sin embargo, si no lo hace, esto puede afectar al uso de nuestra app o los productos y/o servicios ofrecidos a través de él.\n" +
            "Información de terceras partes: Si recibimos información personal sobre usted de un tercero, la protegeremos tal como se establece en esta política de privacidad. Si usted es un tercero que proporciona información personal sobre otra persona, entonces está declarando y garantizando que cuenta con el consentimiento de dicha persona para proporcionarnos la información personal.\n" +
            "Restricciones: Puede optar por restringir la recopilación o el uso de su información personal. Si anteriormente ha aceptado que usemos su información personal con fines de marketing directo, puede cambiar de opinión en cualquier momento comunicándose con nosotros mediante los canales indicados más abajo. Si nos solicita restringir o limitar la forma en que procesamos su información personal, le informaremos cómo la restricción afecta su uso de nuestra app o nuestros productos y servicios.\n" +
            "Acceso y portabilidad de datos: Puede solicitar detalles de la información personal que tenemos sobre usted. Usted puede solicitar una copia de la información personal que tenemos sobre usted. Cuando sea posible, proporcionaremos esta información en formato CSV u otro formato de fácil lectura. Usted puede solicitar que borremos la información personal que tenemos sobre usted en cualquier momento. También puede solicitar que transfiramos esta información personal a un tercero.\n" +
            "Corrección: Si cree que cualquier información que tengamos sobre usted es incorrecta, está desactualizada, es incompleta, es irrelevante o engañosa, comuníquese con nosotros utilizando los canales indicados a continuación. Tomaremos las medidas necesarias para corregir cualquier información que sea inexacta, incompleta, engañosa o esté desactualizada.\n" +
            "Notificación de violaciones de datos: Cumpliremos con las leyes que nos sean aplicables con respecto a cualquier violación de datos.\n" +
            "Reclamaciones: Si usted considera que hemos infringido una ley de protección de datos relevante y desea presentar una queja, comuníquese con nosotros utilizando los canales indicados a continuación y proporcionando todos los detalles de la supuesta infracción. Investigaremos inmediatamente su queja y le responderemos, por escrito, explicando el resultado de nuestra investigación y los pasos que tomaremos para resolver su reclamación. Usted también tiene derecho a comunicarse con un organismo regulador o autoridad de protección de datos en relación con su queja.\n" +
            "Cancelar la suscripción: Para cancelar la suscripción a nuestra base de datos de correos electrónicos o para cancelar la suscripción de las comunicaciones (incluidas las comunicaciones de marketing), comuníquese con nosotros utilizando los canales indicados a continuación o cancele la suscripción utilizando los servicios para ello facilitados en la comunicación.\n" +
            "\n" +
            "7. Cookies\n" +
            "Nuestra política de privacidad cubre el uso de cookies entre su dispositivo y nuestros servidores.\n" +
            "Una cookie es una pequeña porción de datos que una aplicación puede almacenar en su dispositivo, generalmente contiene un identificador único que permite a los servidores de la aplicación reconocer su dispositivo cuando la utiliza; información sobre su cuenta, sesión y/o dispositivo; datos adicionales que sirven para el propósito de la cookie, y cualquier información de mantenimiento sobre la cookie en sí.\n" +
            "Utilizamos cookies para dar a su dispositivo acceso a las funciones principales de nuestra aplicación, para hacer un seguimiento del uso y el rendimiento de la aplicación en su dispositivo, para adaptar su experiencia de nuestra aplicación en función de sus preferencias y para mostrar publicidad en su dispositivo. Cualquier comunicación de datos de cookies entre su dispositivo y nuestros servidores ocurre dentro de un entorno seguro.\n" +
            "\n" +
            "8. Transferencias de negocios\n" +
            "Si nosotros o nuestros activos son adquiridos, o en el improbable caso de que salgamos del negocio o entremos en quiebra, incluiríamos datos, entre los activos transferidos, a cualquier parte que nos adquiera. Usted reconoce que tales transferencias pueden ocurrir y que cualquier tercera parte que nos adquiera puede seguir usando su información personal de acuerdo con esta política.\n" +
            "9. Límites de nuestra Política\n" +
            "Nuestro app puede enlazar con sitios externos que no están gestionados por nosotros. Tenga en cuenta que no tenemos control sobre el contenido y las políticas de esos sitios, y no podemos aceptar responsabilidades acerca de sus respectivas prácticas de privacidad.\n" +
            "\n" +
            "10. Cambios de esta Política\n" +
            "A nuestra discreción, podemos cambiar nuestra política de privacidad para reflejar las prácticas aceptables actuales. Tomaremos las medidas necesarias para informar a los usuarios sobre los cambios a través de nuestro app o el servicio app. El uso continuado de nuestra app después de cualquier cambio en esta política se considerará como aceptación de nuestras prácticas sobre privacidad e información personal.\n" +
            "Si realizamos un cambio significativo en esta política de privacidad, por ejemplo, cambiando una base legal sobre la cual procesamos su información personal, le pediremos que vuelva a dar su consentimiento a la política de privacidad modificada.\n" +
            "Esta política es efectiva desde 10 de mayo de 2020.\n";
    public static List<Survey> SURVEY_LIST = new ArrayList<>();
    public static List<Survey> SURVEY_LIST_FINAL = new ArrayList<>();
    public static Survey ADMIN_SURVEY = new Survey();
    public static List<Question> QUESTION_LIST = new ArrayList<>();
    public static List<User> USER_LIST = new ArrayList<>();
    public static List<UserActivity> ACTIVITY_LIST = new ArrayList<>();
    public static List<ClaimedReward> CLAIMED_LIST = new ArrayList<>();
    public static boolean IS_ADMIN = false;
    public static String PHONE;
    public static Contact CONTACT;
    public static boolean HAS_CLAIMED = false;

    public static HashMap<String,Integer> PARTIAL_INFO;
    public static String ADMIN_TOKEN = "";
    public static SurveyAdapter SURVEYADAPTER;
    public static SurveyAdapter ADMIN_SURVEYADAPTER;

    public static  String getTimeStamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy");
        return simpleDateFormat.format(new Date());
    }

    public static void sendNotification(String token, String body) {
        RootModel rootModel = new RootModel(token, new NotificationModel("SurveyApp", body), new DataModel("Name", "30"));

        ApiInterface apiService =  ApiClient.getClient().create(ApiInterface.class);
        retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendNotification(rootModel);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull retrofit2.Call<ResponseBody> call, @NotNull retrofit2.Response<ResponseBody> response) {
                Log.d("TAG","Successfully notification send by using retrofit.");
            }

            @Override
            public void onFailure(@NotNull retrofit2.Call<ResponseBody> call, @NotNull Throwable t) {

            }
        });
    }
}
