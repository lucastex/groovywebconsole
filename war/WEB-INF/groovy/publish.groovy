import net.tanesha.recaptcha.ReCaptchaImpl
import net.tanesha.recaptcha.ReCaptchaResponse
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.Text

//recaptcha validation
def recaptcha = new ReCaptchaImpl()
recaptcha.publicKey = "6LeJdwgAAAAAAAva5AMuhouqEh4udwUK4U7ZLtwP" 
recaptcha.privateKey = "6LeJdwgAAAAAAI36k1AnU6_YAjE2sD06SCV2Kz_a"
def recaptchaResponse = recaptcha.checkAnswer(request.remoteAddr, params.recaptchaChallengeField, params.recaptchaResponseField)

if (recaptchaResponse.valid) {

	def entity = new Entity("savedscript")
	entity.script = new Text(params.script)
	entity.title = params.title ?: "Untitled"
	entity.author = params.author ?: "Anonymous"
	entity.dateCreated = new Date()
	entity.tags = params.tags?.split(',')*.trim()
	entity.save()

	request.setAttribute('entity', entity)
	redirect "view.groovy?id=${entity.key.id}"
} else {
	request.setAttribute('errorMessage', recaptchaResponse.errorMessage)
    forward 'index.gtpl'
}