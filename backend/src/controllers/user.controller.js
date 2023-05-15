const db = require('../helper/db')
const crypto = require('crypto')

const responseBody = {
  success: false,
  message: 'paramater tidak valid',
  data: []
}

const registerUser = async (req, res) => {
  let { nama, email, password } = req.body
  try {
    let pass = crypto.createHash('md5').update(password).digest('hex')
    let [checkData] = await db.query('SELECT * FROM tb_pengguna WHERE email= :email', { email })
    if(checkData){
      if(Object.keys(checkData).length > 0){
        console.log('email sudah terdaftar');
        responseBody.success = true
        responseBody.message = 'email sudah terdaftar'
      }
    }else {
      let data = await db.query("INSERT INTO tb_pengguna(nama, email, password) VALUES(:nama, :email, :password)", { nama, email, password:pass })
      console.log(data);
      responseBody.success = true
      responseBody.message = 'berhasil registrasi'
    }
  } catch (error) {
    console.log(error);
    responseBody.success = false
    responseBody.message = error
  }
  return res.json(responseBody)
}

const loginUser = async (req, res) => {
  let { email, password } = req.body
  try {
    let pass = crypto.createHash('md5').update(password).digest('hex')
    let data = await db.query('SELECT * FROM tb_pengguna WHERE email= :email AND password= :password', { email, password: pass })
    console.log(data);
    if(Object.keys(data).length === 0){
      responseBody.success = true
      responseBody.message = 'belum mempunyai akun, silahkan registrasi terlebih dahulu'
    }else {
      responseBody.success = true
      responseBody.message = 'berhasil login'
      responseBody.data = data
    }
  } catch (error) {
    responseBody.success = false
    responseBody.message = error
  }
  return res.json(responseBody)
}


module.exports = { registerUser, loginUser }