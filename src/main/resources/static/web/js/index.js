const app = Vue.createApp({
    data(){
        return{
            modal: false,
            modal2: false,
            modal3: false,
            email: "",
            password: "",
            nombre: "",
            apellido: "",
            warningNombre: "",
            warningApellido: "",
            warningEmail: "",
            warningContraseña: "",
        }
    },
    created(){

    },
    methods:{
        iniciarSesion(){
            return axios.post('/api/login',`email=${this.email}&password=${this.password}`).then(response => window.location.href = '/web/accounts.html')
            .catch(function (){
                Swal.fire({
                    icon: 'error',
                    tittle: 'Oops...',
                    text: `Email o contraseña incorrecta`
              })
            })
        },
        registro(){
            passRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\s).{8,15}$/,
            emailRegex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/
            if(this.nombre.length < 3 || this.nombre.length > 10){
                this.warningNombre = "El nombre no es valido"
            }else{
                this.warningNombre = ""
            }
            if(this.apellido.length < 4 || this.apellido.length > 10){
                this.warningApellido = "El apellido no es valido"
            }else{
                this.warningApellido = ""
            }
            if(!emailRegex.test(this.email)){
                this.warningEmail = "El email no es valido"
            }else{
                this.warningEmail = ""
            }
            if(!passRegex.test(this.password)){
                this.warningContraseña = "La contraseña no es valida"
                this.modal3 = true
/*                 alert("La contraseña debe ser minimo de 8 caracteres. Incluir miniscula, mayuscula, número y simbolo") */
            }else{
                this.warningContraseña = ""
                axios.post('/api/clients',`firstName=${this.nombre}&lastName=${this.apellido}&email=${this.email}&password=${this.password}`).then(response => 
                    this.iniciarSesion()
                    ).catch(function (error){
                        let errorData = error.response.data
                        Swal.fire({
                            icon: 'error',
                            tittle: 'Oops...',
                            text: `${errorData}`
                      })
                    })
            }
        },
    }
})
app.mount("#app")