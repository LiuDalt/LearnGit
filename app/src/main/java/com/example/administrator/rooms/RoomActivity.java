package com.example.administrator.rooms;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Database;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class RoomActivity extends AppCompatActivity {

    private View mAddUserView;
    private View mDeleteUserView;
    private View mUpdateUserView;
    private View mQueryUserView;
    private int mId;
    private TextView mUserInfo;
    private List<User> mCurrUserLs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        mAddUserView = findViewById(R.id.add_user);
        mDeleteUserView = findViewById(R.id.delete_user);
        mUpdateUserView = findViewById(R.id.update_user);
        mQueryUserView = findViewById(R.id.query_user);
//        mUserInfo = findViewById(R.id.user_info);

//        UserDatabase.getInstance(this).getUserDao().queryAll().observe(this, userList -> {
//            if(userList != null && !userList.isEmpty()){
//                mUserInfo.setText(userList.get(userList.size() - 1).toString());
//            }
//        });
        mAddUserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable.create(new ObservableOnSubscribe<User>() {
                    @Override
                    public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                        User user = getUser();
                        UserDatabase.getInstance(RoomActivity.this).getUserDao().insert(user);
                        emitter.onNext(user);
                        emitter.onComplete();
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {

                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<User>() {
                            @Override
                            public void accept(User user) throws Exception {

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        }, new Action() {
                            @Override
                            public void run() throws Exception {

                            }
                        });

            }
        });
        mAddUserView.setOnClickListener(v -> {
            Observable.create( emitter -> {
                User user = getUser();
                 UserDatabase.getInstance(RoomActivity.this).getUserDao().insert(user);
                 emitter.onNext(user);
                 emitter.onComplete();
            })
            .subscribeOn(Schedulers.io())
            .doOnSubscribe(disposable -> {})
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(user1 -> {
            }, throwable -> {}, ()->{});
        });

        mDeleteUserView.setOnClickListener(v -> {
            User user = getDeleteUser();
            Observable.create(emitter -> {
                Log.i("room---", "deleting user " + user.toString());
                UserDatabase.getInstance(RoomActivity.this).getUserDao().delete(user);
                emitter.onNext(user);
                emitter.onComplete();
            }).subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            Log.i("room---", "before delete user");
                        }
                    }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
                   .subscribe(u ->{
                        Log.i("room---", "after user delete " + u.toString());
                   }, throwable -> {
                       Log.i("room---", "delete user error");
                   }, () ->{
                       Log.i("room---", "delete user completed");
                   });
        });


        mUpdateUserView.setOnClickListener(v -> {
            Observable.create(emitter -> {
                Log.i("room---", "updating user in " + Thread.currentThread().getId());
                User user = getUser();
                user.name = "updateName";
                UserDatabase.getInstance(RoomActivity.this).getUserDao().update(user);
                emitter.onNext(user);
                emitter.onComplete();
            })
            .subscribeOn(Schedulers.io())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    Log.i("room---","before update user " + Thread.currentThread().getId());
                }
            })
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .map(new Function<Object, String>() {
                @Override
                public String apply(Object o) throws Exception {
                    User us = (User) o;
                    Log.i("room---", "map update user in " + Thread.currentThread().getId());
                    return "map=" + us.getId() + " " + us.getName() + " " + us.getAge();
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(str ->{
                Log.i("room---", "update user= " + str + " thread=" + Thread.currentThread().getId());
            }, throwable -> {
                Log.i("room---"," update user error");
            },() ->{
                Log.i("room---", "update user complete " + Thread.currentThread().getId());
            });
        });

        mQueryUserView.setOnClickListener(v -> {
            Observable.create(emitter -> {
                List<User> users = UserDatabase.getInstance(RoomActivity.this).getUserDao().queryAll();
                if(users == null){
                    emitter.onNext(new ArrayList<>());
                }else {
                    emitter.onNext(users);
                }
                emitter.onComplete();
            })
            .subscribeOn(Schedulers.io())
            .flatMap(new Function<Object, ObservableSource<?>>() {
                @Override
                public ObservableSource<Object> apply(Object o) throws Exception {
                    List<User> users = (List<User>) o;
                    mCurrUserLs = users;
                    Log.i("room---", "curr userLs=" + mCurrUserLs);
                    return Observable.fromIterable(users);
                }
            })
            .filter(u ->{return ((User)u).getId() >= 0;
            })
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(u -> {
                Log.i("room---", "get user-----------");
                User us = (User) u;
                Log.i("room---", "user=" + us.toString());
            });
        });


        testUserList();

    }

    private void testUserList() {
        List<User> users = getUserList();
        findViewById(R.id.add_userlist).setOnClickListener(v -> {
            Observable.create(emitter -> {
                UserDatabase.getInstance(RoomActivity.this).getUserDao().insert(users);
                Log.i("room---", "add userlist threadId=" + Thread.currentThread().getId());
                emitter.onNext(users);

            }).subscribeOn(Schedulers.io()).subscribe(userls -> {
                Log.i("room---", "after add user list=" + users.toString() + " threadId=" + Thread.currentThread().getId());
            });
        });

        findViewById(R.id.delete_userlist).setOnClickListener(v ->{
            Observable.create(emitter -> {
                UserDatabase.getInstance(RoomActivity.this).getUserDao().querySimpleInfo();
                emitter.onNext(new Object());
            }).subscribeOn(Schedulers.io()).subscribe(u ->{
                Log.i("room---", "delete userList ");
            });
        });

        findViewById(R.id.update_userlist).setOnClickListener(v -> {
            Observable.create(emitter -> {
                if(mCurrUserLs != null && !mCurrUserLs.isEmpty()) {
                    changeUserList();
                    UserDatabase.getInstance(RoomActivity.this).getUserDao().updateList(mCurrUserLs);
                    emitter.onNext(new Object());
                }
            }).subscribeOn(Schedulers.io()).subscribe(obj ->{
                Log.i("room---", "update userList ");
            });
        });

        findViewById(R.id.query_simpleinfo).setOnClickListener(v ->{
            Observable.create(emitter -> {
                List<SimpleInfo> simpleInfos = UserDatabase.getInstance(RoomActivity.this).getUserDao().querySimpleInfo();
                emitter.onNext(simpleInfos);
            }).subscribeOn(Schedulers.io()).subscribe(simpleLs -> {
                Log.i("room---", "simpleInfo list=" + simpleLs);
            });
        });


    }

    private void changeUserList() {
        for(User user : mCurrUserLs){
            user.setName(user.getName() + "update");
        }
    }

    private List<User> getUserList() {
        List<User> list = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            User user = new User("userlist" + i, i * 10);
            list.add(user);
        }
        return list;
    }

    private void testThread() {
        Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> emitter) throws Exception {
                Log.i("room---", "从数据库获取数据 ThreadId=" + Thread.currentThread().getId());
                List<User> users = UserDatabase.getInstance(RoomActivity.this).getUserDao().queryAll();
                emitter.onNext(users);
            }
        })
        .subscribeOn(Schedulers.from(Executors.newSingleThreadExecutor()))
        .doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                Log.i("room---", "获取数据之前的回调 ThreadId=" + Thread.currentThread().getId());
            }
        })
        .subscribeOn(AndroidSchedulers.mainThread())
        .flatMap(new Function<List<User>, ObservableSource<?>>() {
            @Override
            public ObservableSource<User> apply(List<User> userList) throws Exception {
                Log.i("room---", "转换观察者对象 ThreadId=" + Thread.currentThread().getId());
                return Observable.fromIterable(userList);
            }
        })
        .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
        .filter(new Predicate<Object>() {
            @Override
            public boolean test(Object obj) throws Exception {
                Log.i("room---", "过滤池id为偶数的数据 ThreadId=" + Thread.currentThread().getId());
                User user = (User) obj;
                return user.getId() % 2 == 0;
            }
        })
        .subscribeOn(Schedulers.computation())
        .take(5)//只选泽其中五个数据
        .subscribeOn(Schedulers.from(Executors.newFixedThreadPool(3)))
        .map(new Function<Object, String>() {
            @Override
            public String apply(Object obj) throws Exception {
                Log.i("room---", "转换User为String ThreadId=" + Thread.currentThread().getId());
                User user = (User) obj;
                return user.toString();
            }
        })
        .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
        .observeOn(Schedulers.from(Executors.newCachedThreadPool()))
        .subscribe(new Consumer<String>() {
            @Override
            public void accept(String userStr) throws Exception {
                Log.i("room---", "观察者获取到数据 +" + userStr + " ThreadId=" + Thread.currentThread().getId());
            }
        });
    }

    private void ttest() {
        Observable.create(new ObservableOnSubscribe<User>() {//被观察者
            @Override
            public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                Log.i("room---", "被观察者存入数据 threadId=" + Thread.currentThread().getId());
                User user = getUser();
                UserDatabase.getInstance(RoomActivity.this).getUserDao().insert(user);//插入数据
                emitter.onNext(user);//通知观察者已经插入数据
                emitter.onComplete();//执行观察者的onComplete方法
            }
        }).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                //被观察者开启插入前的回调
                Log.i("room---", "被观察者插入数据前回调----threadId=" + Thread.currentThread().getId());
            }
        })
        .subscribeOn(Schedulers.io())//被观察者执行在io线程
        .observeOn(AndroidSchedulers.mainThread())//观察者执行在android的UI线程
        .subscribe(new Consumer<User>() {
            @Override
            public void accept(User user) throws Exception {
                //接收到被观察者的通知,和onComplete执行在同一个线程
                Log.i("room---", "存入数据库的数据是：" + user.toString() + " threadId=" + Thread.currentThread().getId());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                //发生错误的时候，观察者的此方法会被执行
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                //观察者的onComplete对象，由被观察者执行（需要被观察者主动调用），和观察者回调执行在统一线程
                Log.i("room---", "插入数据执行完毕 threadId=" + Thread.currentThread().getId());
            }
        });
    }

    private void testQuery() {
        Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> emitter) throws Exception {
                List<User> users = UserDatabase.getInstance(RoomActivity.this).getUserDao().queryAll();
                emitter.onNext(users);
            }
        })
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<List<User>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<User> apply(List<User> userList) throws Exception {
                        return Observable.fromIterable(userList);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object obj) throws Exception {
                        User user = (User) obj;
                        Log.i("room---",user.toString());
                    }
                });
    }

    private void testMap() {
    }

    private void test() {
//        User[] users = new User[]{new User("userA", 12), new User("userB", 32)};
        List<User> userList = new ArrayList<>();
        userList.add(new User("userA", 23));
        userList.add(new User("userB", 24));
        //插入users...
        //...
        //插入完毕后通知监听
        Observable.fromIterable(userList)
        .map(new Function<User, String>() {
            @Override
            public String apply(User user) throws Exception {
                return "mapUserString=" + user.toString();
            }
        })
        .subscribe(new Consumer<String>() {
            @Override
            public void accept(String userStr) throws Exception {
                //接收到被观察者的通知
                    Log.i("room---", "存入数据库的数据是：" + userStr);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                //发生错误的时候，观察者的此方法会被执行
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                //观察者的onComplete对象
                Log.i("room---", "插入数据执行完毕");
            }
        });

    }

    private User getUser() {
        int id = new Random().nextInt(10);
        User user = new User("user" + id, id + 10);
        user.setAddress1(new Address("北京"));
        user.setAddress2(new Address("深圳"));
        return user;
    }

    private User getDeleteUser(){
        User user = getUser();
        user.setId(new Random().nextInt(10));
        return user;
    }

    private void testRoom() {
        for(int i = 0; i < 1; i++){
            User user = new User("User" + i, i + 5);
            Observable.fromArray(user).subscribe(new Consumer<User>() {
                @Override
                public void accept(User user) throws Exception {
                    UserDatabase.getInstance(RoomActivity.this).getUserDao().insert(user);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    Log.i("room---", "error insert");
                }
            });
        }

//        Observable.create(new ObservableOnSubscribe<List<User>>() {
//            @Override
//            public void subscribe(ObservableEmitter<List<User> >emitter) throws Exception {
//                List<User> users = UserDatabase.getInstance(RoomActivity.this).getUserDao().queryAll();
//                emitter.onNext(users);
//                emitter.onComplete();
//            }
//        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<User>>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(List<User> users) {
//                for(User user1 : users){
//                    Log.i("room---", user1.toString());
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.i("room---", "error query " + e);
//            }
//
//            @Override
//            public void onComplete() {
//               Log.i("room---", "query successfully");
//            }
//        });

        Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> emitter) throws Exception {
                Log.i("room---", "subscribe--  " + Thread.currentThread().getId());
                List<User> users = UserDatabase.getInstance(RoomActivity.this).getUserDao().queryAll();
                emitter.onNext(users);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                Log.i("room---", "start query---" + Thread.currentThread().getId());
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation()).subscribe(new Consumer<List<User>>() {
            @Override
            public void accept(List<User> users) throws Exception {
                Log.i("room---", "accept query---" + Thread.currentThread().getId());
                for (User user1 : users) {
                    Log.i("room---", user1.toString());
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i("room---", "query error " + throwable);
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                Log.i("room---", "completed query---" + Thread.currentThread().getId());
            }
        });
    }
}
