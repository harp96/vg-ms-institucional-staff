package pe.edu.vallegrande.vgmsinstitucionalstaff.application.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.WriteResult;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import pe.edu.vallegrande.vgmsinstitucionalstaff.domain.model.InstitucionalStaff;


@Service
public class FirebaseService {

    public UserRecord createFirebaseUser(InstitucionalStaff staff) throws FirebaseAuthException {
        CreateRequest request = new CreateRequest()
                .setEmail(staff.getEmail())
                .setPassword("cetpro2024")
                .setDisplayName(staff.getName())
                .setDisabled(false);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

        saveUserDetailsInFirestore(userRecord, staff);
        assignRoleToUser(userRecord.getUid(), staff.getRol());

        return userRecord;
    }

    public void assignRoleToUser(String uid, String role) throws FirebaseAuthException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        FirebaseAuth.getInstance().setCustomUserClaims(uid, claims);
    }

    private void saveUserDetailsInFirestore(UserRecord userRecord, InstitucionalStaff staff) {
        Firestore db = FirestoreClient.getFirestore();

        Map<String, Object> userData = new HashMap<>();
        userData.put("name", staff.getName());
        userData.put("email", staff.getEmail());
        userData.put("role", staff.getRol());
        userData.put("firstLogin", true);

        ApiFuture<WriteResult> writeResult = db.collection("users").document(userRecord.getUid()).set(userData);

        try {
            WriteResult result = writeResult.get();
            System.out.println("Usuario guardado con éxito. Timestamp: " + result.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error al guardar los detalles del usuario: " + e.getMessage());
        }
    }
}