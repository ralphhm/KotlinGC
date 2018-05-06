The implementation of the SharedPreferences stores change listeners in a WeakHashMap. In a WeakHashMap entries of listeners disappear on garbage collection when there is no strong reference to them. In the [registerOnSharedPreferenceChangeListener](https://developer.android.com/reference/android/content/SharedPreferences#registerOnSharedPreferenceChangeListener(android.content.SharedPreferences.OnSharedPreferenceChangeListener)) documentation it is recommended to store a strong reference to the listener in the using class. In the Kotlin version of the MainActivity it looks as if the val "prefFun"
```` 
private val prefFun: (SharedPreferences, String) -> Unit = { _, _ -> log("prefs changed: function called")}
````
 holds a reference to the same instance that is passed to
````
prefs.registerOnSharedPreferenceChangeListener(prefFun)
````
By decompiling the Kotlin bytecode of the MainActivity into Java code it becomes clear that the passed function is wrapped by a synthetic class implementing the listener interface
````
      Object var3 = this.prefFun;
      if (this.prefFun != null) {
         Object var2 = var3;
         var3 = new MainActivity$sam$android_content_SharedPreferences_OnSharedPreferenceChangeListener$0((Function2)var2);
      }

      var10000.registerOnSharedPreferenceChangeListener((OnSharedPreferenceChangeListener)var3);
````
This way the unreferenced (anonymous) wrapper instance is saved as key in the WeakHashMap and garbage collected.