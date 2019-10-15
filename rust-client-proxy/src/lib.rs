//use client::client_proxy::*;
use client::client_proxy::ClientProxy;
//use client::{client_proxy::ClientProxy, commands::*};
use std::error::Error;
use std::*;

#[cfg(test)]
mod tests {
    #[test]
    fn it_works() {
        assert_eq!(2 + 2, 4);
        println!("run it_works");
    }

    use super::new_client_proxy;
    use super::*;
    #[test]
    fn check_new() {
        //let proxy: ClientProxy = None;

        let ret = panic::catch_unwind(|| new_client_proxy().unwrap()).ok();
        //assert!(ret.unwrap());
        //let ret1 = ret.unwrap_or_else(|| println!(""));
        match ret {
            Some(value) => println!("Result: {:?}", value.accounts),
            None => println!("caught panic"),
        }

        println!("run check new");
    }
}

pub fn new_client_proxy() -> Result<ClientProxy, Box<dyn Error>> {
    let host = "localhost";
    let port: u16 = 8000;
    let validator_set_file = "../../libra/scripts/cli/consensus_peers.config.toml";
    let faucet_account_file = "";
    let sync_on_wallet_recovery = false;
    let faucet_server = None;
    let mnemonic_file = None;

    // new a Client Proxy
    let client = ClientProxy::new(
        host,
        port,
        validator_set_file,
        faucet_account_file,
        sync_on_wallet_recovery,
        faucet_server,
        mnemonic_file,
    )?;

    Ok(client)
}

/// Expose the JNI interface for android below
#[cfg(target_os = "android")]
#[allow(non_snake_case)]
pub mod android {
    use std::ffi::{CStr, CString};
    use std::os::raw::c_char;

    extern crate jni;
    use self::jni::objects::{JClass, JString};
    use self::jni::sys::*;
    use self::jni::JNIEnv;
    use super::*;

    // use config::{config::PersistableConfig, trusted_peers::ConfigHelpers};
    // use libra_wallet::io_utils;
    // use proptest::prelude::*;
    // use tools::tempdir::TempPath;

    // #[no_mangle]
    // pub unsafe extern fn Java_com_mozilla_greetings_RustGreetings_greeting(env: JNIEnv, _: JClass, java_pattern: JString) -> jstring {
    //     // Our Java companion code might pass-in "world" as a string, hence the name.
    //     let world = rust_greeting(env.get_string(java_pattern).expect("invalid pattern string").as_ptr());
    //     // Retake pointer so that we can use it below and allow memory to be freed when it goes out of scope.
    //     let world_ptr = CString::from_raw(world);
    //     let output = env.new_string(world_ptr.to_str().unwrap()).expect("Couldn't create java string!");

    //     output.into_inner()
    // }

    #[no_mangle]
    pub extern "system" fn Java_com_example_rustapp_HelloRust_sayHello(
        env: JNIEnv,
        _: JClass,
        input: JString,
    ) -> jstring {
        let input: String = env
            .get_string(input)
            .expect("Couldn't get Java string")
            .into();

        let output = env
            .new_string(format!("Hello, {}!", input))
            .expect("Couldn't create Java String!");

        output.into_inner()
    }

    /*
     * Class:     org_libra_client_ClientProxy
     * Method:    nativeClientProxyNew
     * Signature: ()J
     */
    #[no_mangle]
    pub unsafe extern "system" fn Java_org_libra_client_ClientProxy_nativeClientProxyNew(
        _: JNIEnv,
        _: JClass,
    ) -> jlong {
        //let global_ref = env.new_global_ref(callback).unwrap();
        //let counter = client_proxy::new();

        //let mut accounts = Vec::new();
        //accounts.reserve(1);
        //let file = TempPath::new();
        //let mnemonic_path = file.path().to_str().unwrap().to_string();
        let mnemonic_path = String::new();
        // let consensus_peer_file = TempPath::new();
        // let consensus_peers_path = consensus_peer_file.path();
        // let (_, consensus_peers_config, _) = ConfigHelpers::gen_validator_nodes(1, None);
        // consensus_peers_config.save_config(&consensus_peers_path);
        // let val_set_file = consensus_peers_path.to_str().unwrap().to_string();
        let val_set_file = "";

        // We don't need to specify host/port since the client won't be used to connect, only to
        // generate random accounts
        let client_proxy = ClientProxy::new(
            "", /* host */
            0,  /* port */
            &val_set_file,
            &"",
            false,
            None,
            Some(mnemonic_path),
        )
        .unwrap();

        Box::into_raw(Box::new(client_proxy)) as jlong
    }

    /*
     * Class:     org_libra_client_ClientProxy
     * Method:    nativeClientProxyDestory
     * Signature: (J)V
     */
    #[no_mangle]
    pub unsafe extern "system" fn Java_org_libra_client_ClientProxy_nativeClientProxyDestory(
        _: JNIEnv,
        _: JClass,
        raw_ptr: jlong,
    ) {
        let _boxed_counter = Box::from_raw(raw_ptr as *mut ClientProxy);
    }
}
