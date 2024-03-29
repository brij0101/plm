package models

import org.codehaus.jackson.annotate.JsonProperty
import reflect.BeanProperty
import javax.persistence.Id
import play.api.Play.current
import play.modules.mongodb.jackson.MongoDB
import scala.collection.JavaConversions._
import net.vz.mongodb.jackson.ObjectId
import play.api.libs.json._
import play.api.libs.json.Json._

class ContactObj(@ObjectId @Id val id: String,
    		@BeanProperty @JsonProperty("userId") val userId: String,
			@BeanProperty @JsonProperty("givenName") val givenName: String,
			@BeanProperty @JsonProperty("fName") val fName: String,
			@BeanProperty @JsonProperty("lName") val lName: String,
			@BeanProperty @JsonProperty("displayName") val displayName: String,
			@BeanProperty @JsonProperty("emailId") val emailId: List[String],
			@BeanProperty @JsonProperty("address") val address: List[String],
			@BeanProperty @JsonProperty("phoneNos") val phoneNos: List[String],
			/*contact.EmailAddresses = new EmailAddressDictionaryEntryType[1];
			EmailAddressDictionaryEntryType email1 = new EmailAddressDictionaryEntryType();
			email1.Key = EmailAddressKeyType.EmailAddress1;
			email1.Value = �testUser@example.com�;
			contact.EmailAddresses[0] = email1;-------
 			@BeanProperty @JsonProperty("Street") val Street:String,
			@BeanProperty @JsonProperty("city") val city: String,
			@BeanProperty @JsonProperty("state") val state: String,
			@BeanProperty @JsonProperty("country") val country: String,
			@BeanProperty @JsonProperty("pincode") val pincode: Long,
			@BeanProperty @JsonProperty("homePh") val homePh: Long,
			@BeanProperty @JsonProperty("workPh") val workPh: Long,
			@BeanProperty @JsonProperty("DirectoryPhoto") val DirectoryPhoto: Boolean,
			*/
			@BeanProperty @JsonProperty("notes") val notes: String,
			@BeanProperty @JsonProperty("bday") val bday: String,
			@BeanProperty @JsonProperty("HasPicture") val HasPicture: Boolean,
			@BeanProperty @JsonProperty("exchangeId") val exchangeId: String,
			@BeanProperty @JsonProperty("folderId") val folderId: String) {
    		@ObjectId @Id def getId = id
    		def this(userId: String,givenName: String,fName:String, lName: String, emailId: List[String],displayName: String,address: List[String],phoneNos: List[String],notes: String,bday: String,HasPicture:Boolean,exchangeId:String, folderId:String) 
    		= this(userId,givenName,fName,lName,emailId,displayName,address,phoneNos,notes,bday,HasPicture,exchangeId,folderId)
}

object Contacts {
    private lazy val db = MongoDB.collection("contacts", classOf[ContactObj], classOf[String])
    def save(c: ContactObj) { db.save(c) }
    def findById(id: String) = {
      try {
    	  Option(db.findOneById(id))
      }
      catch {
        case c: IllegalArgumentException => Option.empty[ContactObj]
      }
    }
    def findByUser(userId: String) = db.find().is("userId", userId).toArray.toList
    
    /*def findByUser(userId: String, start: Int, limit: Int) = {
      val db = MongoDB.collection("contacts", classOf[Contact], classOf[String])
      db.find().is("userId", userId).skip(start).limit(limit).toArray.toList
    }*/
     
    implicit object ContactReads extends Format[ContactObj] {
	    def reads(json: JsValue) = new ContactObj(
	      (json \ "userId").as[String],
	      (json \ "givenName").as[String],
	      (json \ "fName").as[String],
	      (json \ "lName").as[String],
	      (json \ "emailId").as[List[String]],
	      (json \ "displayName").as[String],
	      (json \ "address").as[List[String]],
	      (json \ "phoneNos").as[List[String]],
	      (json \ "notes").as[String],
	      (json \ "bday").as[String],
	      (json \ "HasPicture").as[Boolean],
	      (json \ "exchangeId").as[String],
	      (json \ "folderId").as[String]
	     )
	    
	    def writes(c: ContactObj) = JsObject(Seq(
	      "id" -> JsString(c.id),
	      "userId" -> JsString(c.userId),
	      "givenName" -> JsString(c.givenName),
	      "fName" -> JsString(c.fName),
	      "lName" -> JsString(c.lName),
	      "displayName" -> JsString(c.displayName),
	      "emailId" -> JsArray(c.emailId.map(toJson(_))),
	      "address" -> JsArray(c.address.map(toJson(_))),
	      "phoneNos" -> JsArray(c.phoneNos.map(toJson(_))),
	      "notes" -> JsString(c.notes),
	      "bday" -> JsString(c.bday),
	      "HasPicture" -> JsBoolean(c.HasPicture),
	      "exchangeId" -> JsString(c.exchangeId),
	      "folderId" -> JsString(c.folderId)))
    }
}