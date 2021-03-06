private void askForPermission(String permission, Integer requestCode) {
    if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

            //This is called if user has denied the permission before
            //In this case I am just asking the permission again
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

        } else {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
        }
    } else {
        Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
    }
}

public void ask(View v){
    switch (v.getId()){
        case R.id.location:
            askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);
            break;
        case R.id.call:
            askForPermission(Manifest.permission.CALL_PHONE,CALL);
            break;
        case R.id.write:
            askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXST);
            break;
        case R.id.read:
            askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,READ_EXST);
            break;
        case R.id.camera:
            askForPermission(Manifest.permission.CAMERA,CAMERA);
            break;
        case R.id.accounts:
            askForPermission(Manifest.permission.GET_ACCOUNTS,ACCOUNTS);
            break;
        default:
            break;
    }
}

@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
        switch (requestCode) {
            //Location
            case 1:
                askForGPS();
                break;
            //Call
            case 2:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + "{This is a telephone number}"));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    startActivity(callIntent);
                }
                break;
            //Write external Storage
            case 3:
                break;
            //Read External Storage
            case 4:
                Intent imageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(imageIntent, 11);
                break;
            //Camera
            case 5:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 12);
                }
                break;
            //Accounts
            case 6:
                AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
                Account[] list = manager.getAccounts();
                Toast.makeText(this,""+list[0].name,Toast.LENGTH_SHORT).show();
                for(int i=0; i<list.length;i++){
                    Log.e("Account "+i,""+list[i].name);
                }
        }

        Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
    }else{
        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
    }
}