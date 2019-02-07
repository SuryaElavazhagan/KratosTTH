package kratos.events.kratos_tth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

data class User(var email: String, var phone: Int, var timeStarted: Long)

interface DatabaseCallbacks {
    fun onSuccess()
    fun onFailure()
}

object UserActivity {
    fun getUserID(): String{
        val user = FirebaseAuth.getInstance().currentUser
        return when(user) {
            null -> ""
            else -> user.uid
        }
    }

    fun initialCommitToFirebase(email: String, phone: Int, callbacks: DatabaseCallbacks) {
        val userID = getUserID()
        if(userID != "") {
            val startingTime = Calendar.getInstance().timeInMillis
            val databaseReference = FirebaseDatabase.getInstance().getReference("users")
            databaseReference.child(userID).setValue(User(email, phone, startingTime))
                    .addOnSuccessListener {
                        callbacks.onSuccess()
                    }
                    .addOnFailureListener {
                        callbacks.onFailure()
                    }
        }
    }
}