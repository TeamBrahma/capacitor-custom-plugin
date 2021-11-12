package com.brahma.plugins.custom;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@CapacitorPlugin(name = "Custom",permissions = {
        @Permission(
                alias = "contacts",
                strings = { Manifest.permission.READ_CONTACTS }
        )
})
public class CustomPlugin extends Plugin {

    protected static final int REQUEST_CONTACTS = 12345;
    private Custom implementation = new Custom();
    PluginCall mCall;
    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }

    @PluginMethod()
    public void getContacts(PluginCall call) {
        String value = call.getString("filter");
        // Filter based on the value if want
        mCall = call;
        bridge.saveCall(call);
        if(getPermissionState("contacts")==null){
            requestAllPermissions(call,"contactPermsCallback");
        }else if (getPermissionState("contacts") != PermissionState.GRANTED) {
            requestAllPermissions(call,"contactPermsCallback");
        } else {
            loadContacts(call);
        }
    }

    @PermissionCallback
    private void contactPermsCallback(PluginCall call) {
        if (getPermissionState("contacts") == PermissionState.GRANTED) {
            loadContacts(call);
        } else {
            call.reject("Permission is required to take a picture");
        }
    }

    void loadContacts(PluginCall call) {
        ArrayList<Map> contactList = new ArrayList<>();
        ContentResolver cr = this.getContext().getContentResolver();

        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                Map<String,String> map =  new HashMap<String, String>();

                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                map.put("firstName", name);
                map.put("lastName", "");

                String contactNumber = "";

                if (cur.getInt(cur.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    pCur.moveToFirst();
                    contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Log.i("phoneNUmber", "The phone number is "+ contactNumber);
                }
                map.put("telephone", contactNumber);
                contactList.add(map);
            }
        }
        if (cur != null) {
            cur.close();
        }

        JSONArray jsonArray = new JSONArray(contactList);
        JSObject ret = new JSObject();
        ret.put("results", jsonArray);
        call.resolve(ret);
    }
}
