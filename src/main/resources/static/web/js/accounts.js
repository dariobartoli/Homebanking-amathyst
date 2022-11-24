const app = Vue.createApp ({
    data(){
        return{
            urlApi:"http://localhost:8080/api/clients/current",
            titulo:"cuentas",
            cuentas: [],
            cuentaMelba: [],
            prestamos: [],
            modal: false,
            modal2: false,
            tipoCuenta: "",
            cuentasActivas: [],
            numeroCuenta: "",
        }
    },
    created(){
        this.getdata()
    },
    methods:{
        getdata(){
            axios.get(this.urlApi)
            .then(data => {
                this.cuentaMelba = data.data
                this.prestamos = this.cuentaMelba.loans.sort((a,b) => a.id - b.id)
                this.cuentas = this.cuentaMelba.accounts.sort((a,b) => a.id - b.id)
                this.cuentasActivas = this.cuentaMelba.accounts.filter(cuenta => cuenta.active == true)
                console.log(this.cuentas)
                console.log(this.cuentasActivas)
            })
        },
        modificarSaldo(saldo){
            return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(saldo);
        },
        modificarFecha(fecha){
            let fechaNueva = fecha.split("T")
            let fechaNueva2 = fechaNueva[0].split("-")
            return `${fechaNueva2[2]}-${fechaNueva2[1]}-${fechaNueva2[0]}`
        },
        LogOut(){
            axios.post('/api/logout').then(response=> window.location.href = "/web/index.html")
        },
        crearCuenta(){
            Swal.fire({
                title: '¡Cuidado!',
                text: "¿Estás seguro de crear una cuenta nueva?",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#7b4aee',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si, crear cuenta',
                cancelButtonText: 'Cancelar',
              }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/clients/current/accounts', `type=${this.tipoCuenta}`).catch(function (error){
                        let errorData = error.response.data
                        Swal.fire({
                            icon: 'error',
                            tittle: 'Oops...',
                            text: `${errorData}`
                        })
                    })
                  Swal.fire({
                    title: 'Cuenta creada con éxito',
                    icon: 'success',
                    confirmButtonColor: '#7b4aee'
                  }).then(response=> this.getdata())
                }
              })
        },
        eliminarCuenta(){
            Swal.fire({
                title: '¡Cuidado!',
                text: "¿Estás seguro de eliminar esta cuenta?",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#7b4aee',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si, eliminar cuenta',
                cancelButtonText: 'Cancelar',
              }).then((result) => {
                if (result.isConfirmed) {
                    axios.patch('/api/clients/current/accounts/delete', `number=${this.numeroCuenta}`).catch(function (error){
                        let errorData = error.response.data
                        Swal.fire({
                            icon: 'error',
                            tittle: 'Oops...',
                            text: `${errorData}`
                        })
                    })
                  Swal.fire({
                    title: 'Cuenta eliminada',
                    icon: 'error',
                    confirmButtonColor: '#7b4aee'
                  }).then(response=> this.getdata())
                }
              })
        },
    },
})
app.mount('#app')
