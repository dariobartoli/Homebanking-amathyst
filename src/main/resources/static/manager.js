const { createApp } = Vue
let app = createApp({
    data(){
        return{
            clientes: [],
            clientes2: [],
            nombre: "",
            apellido: "",
            email: "",
            editar: "",
        }
    },
    created(){
        this.getdata()
    },
    mounted(){
    },
    methods:{
        getdata(){
            axios.get("http://localhost:8080/clients")
            .then(data => {
                this.clientes = data.data._embedded.clients
                this.clientes2 = data.data
                console.log(this.clientes)
            })
            .catch(error => console.error(error));
        },
        addClient(){
            if(this.nombre.length>0 && this.apellido.length>0 && this.email.length>0){
                axios.post("http://localhost:8080/clients", {
                    firstName: this.nombre,
                    lastName: this.apellido,
                    email: this.email
                }).then(()=> location.reload())
            }
        },
        deleteClient(id){
            axios.delete(id)
            .then(() =>{
                this.getdata()
            })
        },
        editClient(id){
            if(this.nombre.length>0 && this.apellido.length>0 && this.email.length>0){
                axios.put(id, {
                    firstName: this.nombre,
                    lastName: this.apellido,
                    email: this.email
                }).then(()=> location.reload())
            }
        }
    },
    computed:{

    },
})
app.mount('#app')