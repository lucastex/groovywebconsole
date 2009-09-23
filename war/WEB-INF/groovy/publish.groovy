import net.tanesha.recaptcha.ReCaptchaImpl
import net.tanesha.recaptcha.ReCaptchaResponse
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.Text

//recaptcha validation
def recaptcha = new ReCaptchaImpl()
recaptcha.privateKey = "6LfvzgcAAAAAAFEemOWIGyAR61vaL19u0lj9cSCn"
recaptcha.publicKey = "6LfvzgcAAAAAAEdYwARtbSk8kr2aoSYRSFuFE6P6"
def recaptchaResponse = recaptcha.checkAnswer(request.remoteAddr, params.recaptchaChallengeField, params.recaptchaResponseField)



def entity = new Entity("savedscript")
entity.script = new Text(params.script)
//entity.title = params.title ?: "Untitled"

entity.title = String.valueOf(recaptchaResponse.valid)

entity.author = params.author ?: "Anonymous"
entity.dateCreated = new Date()
entity.tags = params.tags?.split(',')*.trim()
entity.save()

request.setAttribute('entity', entity)

redirect "view.groovy?id=${entity.key.id}"