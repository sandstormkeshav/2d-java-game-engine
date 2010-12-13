package engine.game;

/**
 *
 * @author Philipp
 */
public class Keymapping {

    Key[] key;

    public Keymapping(Key[] key){
        //Initialize array:
        this.key = new Key[key.length];
        this.key = key;

    }

    public int getKey(String keyName){

        for(int i = 0; i < key.length; i++){

            //return charNumber if key found:
            if(keyName.equals(key[i].name)){
                return key[i].charNumber;
            }

        }

        //key not found:
        return -1;
    }

    public boolean keyPressed(String keyName){

        for(int i = 0; i < key.length; i++){
            //search for key:
            if(keyName.equals(key[i].name)){
                //return if key is Pressed:
                if(gameMain.keyPressed[key[i].charNumber]){
                    return true;
                }
                else{
                    return false;
                }
            }
        }

        //also return false if key not found:
        return false;
    }

    public boolean keyReleased(String keyName){

        for(int i = 0; i < key.length; i++){
            //search for key:
            if(keyName.equals(key[i].name)){
                //return if key is Pressed:
                if(gameMain.keyReleased[key[i].charNumber]){
                    return true;
                }
                else{
                    return false;
                }
            }
        }

        //also return false if key not found:
        return false;
    }

    public void addKey(Key newKey){

        Key[] tempKey = key;
        key = new Key[tempKey.length+1];

        for(int i = 0; i < tempKey.length; i++){
            key[i] = tempKey[i];
        }

        key[tempKey.length] = newKey;

    }

    public void removeKey(String keyName){

        int a = 0;

        Key[] tempKey = key;
        key = new Key[tempKey.length-1];

        for(int i = 0; i < tempKey.length; i++){
            //search for key:
            if(keyName.equals(tempKey[i].name)){
                //skip if this is the entered key
            }
            else{
                key[a] = tempKey[i];
                a++;
            }
        }

    }

}
