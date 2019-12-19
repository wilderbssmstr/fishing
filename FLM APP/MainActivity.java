private boolean checkMapServices(){

        if(isServicesOK()){

            if(isMapsEnabled()){

                return true;

            }

        }

        return false;

    }



    private void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")

                .setCancelable(false)

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);

                    }

                });

        final AlertDialog alert = builder.create();

        alert.show();

    }



    public boolean isMapsEnabled(){

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );



        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {

            buildAlertMessageNoGps();

            return false;

        }

        return true;

    }



    private void getLocationPermission() {

        /*

         * Request location permission, so that we can get the location of the

         * device. The result of the permission request is handled by a callback,

         * onRequestPermissionsResult.

         */

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),

                android.Manifest.permission.ACCESS_FINE_LOCATION)

                == PackageManager.PERMISSION_GRANTED) {

            mLocationPermissionGranted = true;

            getChatrooms();

        } else {

            ActivityCompat.requestPermissions(this,

                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},

                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        }

    }



    public boolean isServicesOK(){

        Log.d(TAG, "isServicesOK: checking google services version");



        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);



        if(available == ConnectionResult.SUCCESS){

            //everything is fine and the user can make map requests

            Log.d(TAG, "isServicesOK: Google Play Services is working");

            return true;

        }

        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){

            //an error occured but we can resolve it

            Log.d(TAG, "isServicesOK: an error occured but we can fix it");

            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);

            dialog.show();

        }else{

            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();

        }

        return false;

    }



    @Override

    public void onRequestPermissionsResult(int requestCode,

                                           @NonNull String permissions[],

                                           @NonNull int[] grantResults) {

        mLocationPermissionGranted = false;

        switch (requestCode) {

            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {

                // If request is cancelled, the result arrays are empty.

                if (grantResults.length > 0

                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mLocationPermissionGranted = true;

                }

            }

        }

    }



    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: called.");

        switch (requestCode) {

            case PERMISSIONS_REQUEST_ENABLE_GPS: {

                if(mLocationPermissionGranted){

                    getChatrooms();

                }

                else{

                    getLocationPermission();

                }

            }

        }



    }