package com.vssnake.potlach.server.repository;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.vssnake.potlach.server.model.Gift;
import com.vssnake.potlach.server.model.GiftCreator;
import com.vssnake.potlach.server.model.SpecialInfo;
import com.vssnake.potlach.server.model.User;

public class MemoryPotlachRepository implements PotlachRepository{
	
	private static final String IP = "http://192.168.1.108";
	
	
	Map<String,User> userDatabase;
	HashMap<Long,Gift> mGiftDatabase;
	SpecialInfo mSpecial;
	
	@Autowired
	TokenRepository tokenRepository;
	
	public MemoryPotlachRepository(){
		mGiftDatabase = new HashMap<Long, Gift>();
		userDatabase = new LinkedHashMap<String, User>();
		mSpecial = new SpecialInfo();
		init();
	}
	
	private void init(){
		userDatabase.put("virtual.solid.snake@gmail.com",
                new User("virtual.solid.snake@gmail.com","Realkone",true,null,null,
                        "http://img2.meristation.com/files/imagenes/general/mgs4_estara_disponible_manana_martes_17_en_espana_.28276.jpg"
                        ));
		userDatabase.put("pepitoGrillo@gmail.com",
                new User("pepitoGrillo@gmail.com","Pepito grillo",true,null,null,
                        "http://eidease.com/wp-content/uploads/2014/02/4466c312099b30875585341d15e7a0c0.png"));
		userDatabase.put("juanPalomo@gmail.com",
                new User("juanPalomo@gmail.com","Juan Palomo",true,null,null,
                        "http://img.desmotivaciones.es/201012/Juanpalomo_1.jpg"));
		userDatabase.put("aranoka@gmail.com",new User("aranoka@gmail.com","Lorena",true,null,null,
                "http://img.desmotivaciones.es/201103/lamonavestidadeseda.jpg"));

		mGiftDatabase.put(1l,new Gift(1l,"virtual.solid.snake@gmail.com",
                "River","The nightfall is beautifull",
                IP +"/photos/test1.jpg",
                IP +"/photos/test1_t.jpg"));
		userDatabase.get("virtual.solid.snake@gmail.com").addGift(1l);

		mGiftDatabase.put(2l,new Gift(2l,"aranoka@gmail.com",
                "CAT","CAT CAT CAT CAT CAT CAT CAT CAT",
                IP +"/photos/test2.jpg",
                IP +"/photos/test2_t.jpg"));		
		
		userDatabase.get("aranoka@gmail.com").addGift(2l);

		mGiftDatabase.put(3l,new Gift(3l,"virtual.solid.snake@gmail.com",
                "Bear","I wanna Hug",
                IP +"/photos/test3.jpg",
                IP +"/photos/test3_t.jpg"));
		userDatabase.get("virtual.solid.snake@gmail.com").addGift(3l);

        mGiftDatabase.put(4l,new Gift(4l,"pepitoGrillo@gmail.com",
                "Bird","Bird singing",
                IP +"/photos/test4.jpg",
                IP +"/photos/test4_t.jpg"));
        userDatabase.get("pepitoGrillo@gmail.com").addGift(4l);

        mGiftDatabase.put(5l,new Gift(5l,"pepitoGrillo@gmail.com",
                "Night Sky","",
                IP +"/photos/test5.jpg",
                IP +"/photos/test5_t.jpg"));
        userDatabase.get("pepitoGrillo@gmail.com").addGift(5l);
        
        mGiftDatabase.get(1l).addNewChain(2l);
		mGiftDatabase.get(1l).addNewChain(3l);
	}
	
	@Override
	public User addUser(User user) {
		// TODO Auto-generated method stub
		return userDatabase.put(user.getEmail(), user);
	}

	@Override
	public User getUser(String email) {
		// TODO Auto-generated method stub
		return userDatabase.get(email);
	}
	
	@Override
	public User getUserWithToken(String token) {
		// TODO Auto-generated method stub
		String email =  tokenRepository.getEmail(token);
		if (email != null){
			return userDatabase.get(email);
		}
		return null;
	}
	
	@Override
	public Collection<Gift> findByTitle(String title) {
		List<Gift> gifts = new ArrayList<Gift>();
        for (Map.Entry<Long, Gift> e : mGiftDatabase.entrySet()) {
            if (e.getValue().getTitle().toLowerCase().startsWith(title.toLowerCase())) {
                gifts.add(e.getValue());
            }
        }
       
		return gifts;
	}

	@Override
	public Collection<Gift> getLastGifts(String token,int startBy) {
		List<Gift> mGiftReturn = new ArrayList<Gift>();
		User user = getUserWithToken(token);
		boolean restricted = user.getHideInappropriate();
		if (startBy < mGiftDatabase.size()){
			
			Iterator<Entry<Long,Gift>> giftIterator = mGiftDatabase.entrySet().iterator();
			int i = startBy;
			while (giftIterator.hasNext() && i < startBy + 20){
				if (i>=startBy){
					Gift gift = giftIterator.next().getValue();
					if (!restricted || !gift.getObscene()){
						mGiftReturn.add(gift);
					}
				}
				
				i++;
			}
		}
		return mGiftReturn;
	}
	@Override
	public Gift createGift(GiftCreator giftCreator, Long idChain,String token) {
		// TODO Auto-generated method stub
		User user = getUserWithToken(token);
		if(user != null){


            String photoUri = saveNewPhoto(giftCreator.getImageBytes(),"");
            String photoThumbUri = saveNewPhoto(giftCreator.getImageThumbBytes(),
                    "thumb");

            Iterator<Entry<Long,Gift>> giftIterator = mGiftDatabase.entrySet().iterator();
            long cont = 1;
			while (giftIterator.hasNext()){
				Entry<Long,Gift> entry = giftIterator.next();
				if (!entry.getKey().equals(cont)){
					break;
				}
				cont++;
			}
            Gift gift = new Gift(cont,
                    giftCreator,
                    IP +photoUri,
                    IP +photoThumbUri);
            
            gift.setUserEmail(user.getEmail());

            mGiftDatabase.put(gift.getId(),gift);
            if (idChain != -1){ //No chain
            	mGiftDatabase.get(idChain).addNewChain(gift.getId());
            }

            user.addGift(gift.getId());
            return gift;

        }
		return null;
	}

	@Override
	public Gift getGift(Long id) {
		// TODO Auto-generated method stub
		return mGiftDatabase.get(id);
	}

	@Override
	public Collection<Gift> showUserGift(String email) {
		List<Gift> listGifts = new ArrayList<Gift>();
	
		if (userDatabase.containsKey(email)){
			List<Long> idGiftsUser = userDatabase.get(email).getGiftPosted();
            for (int i = 0; i<idGiftsUser.size();i++){
                if(mGiftDatabase.containsKey(idGiftsUser.get(i))){
                	listGifts.add(mGiftDatabase.get(idGiftsUser.get(i)));
                }
            }
		}
		
		return listGifts;
	}
	
	@Override
	public Collection<Gift> showGiftChain(Long idGift) {
		List<Gift> listGifts = new ArrayList<Gift>();
        if (mGiftDatabase.containsKey(idGift)) {
            Gift gift = mGiftDatabase.get(idGift);
            List<Long> giftChainIds = gift.getChainsID();
            for (int i =0; giftChainIds.size() > i; i++){
            	listGifts.add(mGiftDatabase.get(giftChainIds.get(i)));
            }
        }
		return listGifts;
	}


	@Override
	public boolean deleteGift(Long id,User user) {
		if (!user.giftExist(id)){
			return false;
		}
		if (mGiftDatabase.containsKey(id)){
			Gift gift = mGiftDatabase.get(id);
			List<Long> idGiftChain = new ArrayList<Long>();
			int cont = 0;
			idGiftChain = gift.getChainsID();
			while (idGiftChain.size() > cont){//First delete giftChain
				if (mGiftDatabase.containsKey(idGiftChain.get(cont))){
					Gift giftToDelete = mGiftDatabase.get(idGiftChain.get(cont));
					idGiftChain.addAll(giftToDelete.getChainsID());
					Iterator<Entry<String,User>> userIterator = userDatabase.entrySet().iterator();
					while (userIterator.hasNext()){
						Entry<String,User> nextUser = userIterator.next();
						nextUser.getValue().removeGift(giftToDelete.getId());
					}
					mGiftDatabase.remove(idGiftChain.get(cont));
					cont++;
				}
			}
			mGiftDatabase.remove(id); //To the end remove the gift
			Iterator<Entry<String,User>> userIterator = userDatabase.entrySet().iterator();
			while (userIterator.hasNext()){
				Entry<String,User> nextUser = userIterator.next();
				nextUser.getValue().removeGift(id);
				nextUser.getValue().removeLike(id);
			}
			mGiftDatabase.remove(id);
			return true;
		}
		return false;
	}
	
	public SpecialInfo showSpecialInfo(){
		SpecialInfo special = new SpecialInfo();
		
		Iterator<Entry<String,User>> userIterator = userDatabase.entrySet().iterator();
		Map<String,Integer> usersEmail = new HashMap<String, Integer>();
		while (userIterator.hasNext()){
			Entry<String, User> user = userIterator.next();
			usersEmail.put(user.getValue().getEmail(), user.getValue().getGiftPosted().size());
		}
		Map<String,Integer> userEmailOrdered = sortByComparator(usersEmail);
		
		Iterator<Entry<String,Integer>> emailIterator = userEmailOrdered.entrySet().iterator();
		
		int cont = 0;
		User[] specialUsers = new User[3];
		while (emailIterator.hasNext()){
			if (cont < 3){
				Entry<String, Integer> user = emailIterator.next();
				specialUsers[cont] = userDatabase.get(user.getKey());
				cont++;
			}else{
				break;
			}
		}
		
		special.setUsersOfTheDay(specialUsers);
		
		
		Iterator<Entry<Long,Gift>> giftIterator = mGiftDatabase.entrySet().iterator();
		Map<Long,Long> giftsId = new HashMap<Long, Long>();
		while (giftIterator.hasNext()){
			Entry<Long, Gift> gift = giftIterator.next();
			giftsId.put(gift.getValue().getId(), gift.getValue().getViewCounts());
		}
		Map<Long,Long> giftsOrdered = sortByComparatorGift(giftsId);
		
		Iterator<Entry<Long,Long>> giftsOrdereIterator = giftsOrdered.entrySet().iterator();
		
		cont = 0;
		Gift[] specialGifts = new Gift[3];
		while (giftsOrdereIterator.hasNext()){
			if (cont < 3){
				Entry<Long, Long> gift = giftsOrdereIterator.next();
				specialGifts[cont] = mGiftDatabase.get(gift.getKey());
				cont++;
			}else{
				break;
			}
		}
		special.setGiftsOfTheDay(specialGifts);
		
		
		return special;
	}
	private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap) {
		 
		// Convert Map to List
		List<Map.Entry<String, Integer>> list = 
			new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());
 
		// Sort list with comparator, to compare the Map values
		Comparator<Map.Entry<String, Integer>> comparator;
		comparator=Collections.reverseOrder(new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
                                           Map.Entry<String, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
		Collections.sort(list, comparator);
 
		// Convert sorted map back to a Map
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	private static Map<Long, Long> sortByComparatorGift(Map<Long, Long> unsortMap) {
		 
		// Convert Map to List
		List<Map.Entry<Long, Long>> list = 
			new LinkedList<Map.Entry<Long, Long>>(unsortMap.entrySet());
 
		// Sort list with comparator, to compare the Map values
		Comparator<Map.Entry<Long, Long>> comparator;
		comparator = Collections.reverseOrder(new Comparator<Map.Entry<Long, Long>>() {
			public int compare(Map.Entry<Long, Long> o1,
                                           Map.Entry<Long, Long> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
		Collections.sort(list, comparator);
 
		// Convert sorted map back to a Map
		Map<Long, Long> sortedMap = new LinkedHashMap<Long, Long>();
		for (Iterator<Map.Entry<Long, Long>> it = list.iterator(); it.hasNext();) {
			Map.Entry<Long, Long> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	//The worst imp of the World ;-p (No time)
	private  String  saveNewPhoto(byte[] photo,String sufix){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_" + sufix + ".jpg";
        
        
        File potlachBase = new File("src/main/webapp/photos");  //Shit made
        potlachBase.mkdirs();

        File potlachImage = new File(potlachBase,imageFileName);


        OutputStream out = null;
        try {
            potlachImage.createNewFile();
            InputStream in = new ByteArrayInputStream(photo);
            out = new FileOutputStream(potlachImage);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "/photos/"+imageFileName;




    }

	


	


}
